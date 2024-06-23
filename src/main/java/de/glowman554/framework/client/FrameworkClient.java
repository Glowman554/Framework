package de.glowman554.framework.client;

import de.glowman554.config.ConfigManager;
import de.glowman554.framework.client.command.CommandManager;
import de.glowman554.framework.client.command.impl.ProfileCommand;
import de.glowman554.framework.client.command.impl.SetHackedCommand;
import de.glowman554.framework.client.command.impl.UuidCommand;
import de.glowman554.framework.client.commandshortcuts.CommandShortcutsManager;
import de.glowman554.framework.client.config.Processors;
import de.glowman554.framework.client.discord.RichPresence;
import de.glowman554.framework.client.event.EventManager;
import de.glowman554.framework.client.event.EventTarget;
import de.glowman554.framework.client.event.impl.ModRegisterEvent;
import de.glowman554.framework.client.hud.HUDConfigScreen;
import de.glowman554.framework.client.hud.HUDManager;
import de.glowman554.framework.client.mod.Mod;
import de.glowman554.framework.client.mod.impl.*;
import de.glowman554.framework.client.registry.FrameworkRegistries;
import de.glowman554.framework.client.screen.CommandShortcutScreen;
import de.glowman554.framework.client.screen.ModSelectionScreen;
import de.glowman554.framework.client.telemetry.TelemetryManager;
import de.glowman554.framework.client.telemetry.buildin.TelemetryDiscordUserCollector;
import de.glowman554.framework.client.telemetry.buildin.TelemetryFabricModCollector;
import de.glowman554.framework.client.telemetry.buildin.TelemetryModCollector;
import de.glowman554.framework.client.utils.DirectoryUtils;
import de.glowman554.framework.data.Data;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class FrameworkClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(FrameworkClient.class);
    private static FrameworkClient instance;
    private HUDManager hudManager;
    private CommandManager commandManager;
    private ConfigManager configManager;
    private FrameworkConfig config;
    private TelemetryManager telemetryManager;
    private CommandShortcutsManager commandShortcutsManager;
    private RichPresence richPresence;
    private ConfigManager modsManager;

    public FrameworkClient() {
        instance = this;
    }

    public static FrameworkClient getInstance() {
        return instance;
    }

    @Override
    public void onInitializeClient() {
        LOGGER.info("Thanks for using Framework by Glowman554 <3");

        EventManager.register(this);
        Processors.register();

        File dataFolder = new File("framework");
        DirectoryUtils.createDirectory(dataFolder);

        ConfigManager.BASE_FOLDER = dataFolder;

        configManager = new ConfigManager("config", false);
        try {
            config = (FrameworkConfig) configManager.loadValue("client", new FrameworkConfig());
        } catch (IllegalArgumentException e) {
            config = new FrameworkConfig();
        }
        saveConfig();

        if (config.development.singleModFile) {
            modsManager = new ConfigManager("mods", false);
        } else {
            DirectoryUtils.createDirectory(new File(dataFolder, "mods"));
        }

        if (config.development.singleModFile) {
            LOGGER.info("Running data generators");
            Data.generate();
        }

        ServerInfoFeatured.load(config.development.featuredServersBackend);

        new FrameworkKeyBinding("key.framework.hud", GLFW.GLFW_KEY_H, FrameworkKeyBinding.MISC, HUDConfigScreen::open);
        new FrameworkKeyBinding("key.framework.modselect", GLFW.GLFW_KEY_M, FrameworkKeyBinding.MISC,
                ModSelectionScreen::open);
        new FrameworkKeyBinding("key.framework.command_shortcuts", GLFW.GLFW_KEY_B, FrameworkKeyBinding.MISC,
                CommandShortcutScreen::open);

        keybinding("hide-players");
        keybinding("discord-chat");
        keybinding("queue-notifier");
        keybinding("rainbow");
        keybinding("entity-esp");
        keybinding("fullbright");
        keybinding("xray");
        keybinding("auto-sprint");
        keybinding("auto-sprint-jump");
        keybinding("auto-leave");
        keybinding("twerk");

        commandManager = new CommandManager(config.prefix);
        commandManager.addCommand("uuid", new UuidCommand());
        commandManager.addCommand("profile", new ProfileCommand());
        commandManager.addCommand("set-hacked", new SetHackedCommand());

        commandShortcutsManager = new CommandShortcutsManager();

        telemetryManager = new TelemetryManager();
        if (config.telemetry.debug) {
            telemetryManager.setDebug(true);
        }
        // telemetryManager.addEndpoint(new URL("https://telemetry.glowman554.de/"));
        try {
            telemetryManager.addEndpoint(new URL(config.development.telemetryCollectorBackend));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        richPresence = new RichPresence();
        richPresence.start();

        FabricLoader.getInstance().getEntrypointContainers("framework", FrameworkEntrypoint.class)
                .forEach(extension -> {
                    FrameworkEntrypoint entrypoint = extension.getEntrypoint();
                    LOGGER.info("Calling entrypoint {}", entrypoint.getClass().getName());
                    entrypoint.initialize();
                });

        FrameworkRegistries.TELEMETRY_COLLECTORS.register(TelemetryFabricModCollector.class,
                new TelemetryFabricModCollector());
        FrameworkRegistries.TELEMETRY_COLLECTORS.register(TelemetryModCollector.class, new TelemetryModCollector());
        FrameworkRegistries.TELEMETRY_COLLECTORS.register(TelemetryDiscordUserCollector.class,
                new TelemetryDiscordUserCollector());
    }

    private void keybinding(String modId) {
        new FrameworkKeyBinding("key.framework." + modId, -1, FrameworkKeyBinding.MODS, null);
    }

    @EventTarget
    public void onModRegister(ModRegisterEvent event) {
        register(new ModHidePlayers());
        register(new ModDiscordChat());
        register(new ModQueueNotifier());
        register(new ModArmorStatus());
        register(new ModFpsDisplay());
        register(new ModRainbowText());
        register(new ModPositionDisplay());
        register(new ModMemoryDisplay());
        register(new ModPingDisplay());
        register(new ModEntityESP());
        register(new ModFullBright());
        register(new ModNoTelemetry());
        register(new ModXRay());
        register(new ModModList());
        register(new ModCriticals());
        register(new ModAutoSprint());
        register(new ModAntiBreak());
        register(new ModAutoSprintJump());
        register(new ModAutoEat());
        register(new ModAutoTotem());
        register(new ModAutoRespawn());
        register(new ModDeathPositionSaver());
        register(new ModAutoLeave());
        register(new ModTips());
        register(new ModTwerk());
        register(new ModMenuBackground());
        register(new ModHeartView());
    }

    private void register(Mod mod) {
        FrameworkRegistries.MODS.register(mod.getClass(), mod);
    }

    public void start() {
        hudManager = HUDManager.getInstance();
        new ModRegisterEvent().call();
        telemetryManager.start();
    }

    public void saveConfig() {
        configManager.setValue("client", config);
    }

    public FrameworkConfig getConfig() {
        return config;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public HUDManager getHudManager() {
        return hudManager;
    }

    public TelemetryManager getTelemetryManager() {
        return telemetryManager;
    }

    public CommandShortcutsManager getCommandShortcutsManager() {
        return commandShortcutsManager;
    }

    public RichPresence getRichPresence() {
        return richPresence;
    }

    public ConfigManager getModsManager() {
        return modsManager;
    }
}
