package de.toxicfox.framework.client;

import de.toxicfox.config.ConfigManager;
import de.toxicfox.config.auto.AutoSavable;
import de.toxicfox.framework.client.command.CommandManager;
import de.toxicfox.framework.client.command.impl.ProfileCommand;
import de.toxicfox.framework.client.command.impl.SetHackedCommand;
import de.toxicfox.framework.client.command.impl.TokenCommand;
import de.toxicfox.framework.client.command.impl.UuidCommand;
import de.toxicfox.framework.client.commandshortcuts.CommandShortcutsManager;
import de.toxicfox.framework.client.config.Processors;
import de.toxicfox.framework.client.event.EventManager;
import de.toxicfox.framework.client.event.EventTarget;
import de.toxicfox.framework.client.event.impl.ModRegisterEvent;
import de.toxicfox.framework.client.hud.HUDConfigScreen;
import de.toxicfox.framework.client.hud.HUDManager;
import de.toxicfox.framework.client.mod.Mod;
import de.toxicfox.framework.client.mod.impl.*;
import de.toxicfox.framework.client.registry.FrameworkRegistries;
import de.toxicfox.framework.client.screen.CommandShortcutScreen;
import de.toxicfox.framework.client.screen.ModSelectionScreen;
import de.toxicfox.framework.client.telemetry.TelemetryManager;
import de.toxicfox.framework.client.telemetry.buildin.TelemetryFabricModCollector;
import de.toxicfox.framework.client.telemetry.buildin.TelemetryModCollector;
import de.toxicfox.framework.client.telemetry.buildin.TelemetrySystemCollector;
import de.toxicfox.framework.client.utils.DirectoryUtils;
import de.toxicfox.framework.data.Data;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

        AutoSavable.strict = false;

        configManager = new ConfigManager("config", false);
        try {
            config = (FrameworkConfig) configManager.loadValue("client", new FrameworkConfig());
        } catch (IllegalArgumentException e) {
            config = new FrameworkConfig();
        }
        saveConfig();

        if (config.development.debugAutoSavable) {
            AutoSavable.debug = LOGGER::info;
        }

        if (config.development.singleModFile) {
            modsManager = new ConfigManager("mods", false);
        } else {
            DirectoryUtils.createDirectory(new File(dataFolder, "mods"));
        }

        if (config.development.runGenerators) {
            LOGGER.info("Running data generators");
            Data.generate();
        }

        try {
            extractDefaultProfile("default");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ServerInfoFeatured.load(config.development.backend.featuredServers);

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
        commandManager.addCommand("token", new TokenCommand());

        EventManager.register(commandManager);

        commandShortcutsManager = new CommandShortcutsManager();

        telemetryManager = new TelemetryManager();
        if (config.telemetry.debug) {
            telemetryManager.setDebug(true);
        }

        try {
            telemetryManager.addEndpoint(new URL(config.development.backend.telemetryCollector));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        FabricLoader.getInstance().getEntrypointContainers("framework", FrameworkEntrypoint.class)
                .forEach(extension -> {
                    FrameworkEntrypoint entrypoint = extension.getEntrypoint();
                    LOGGER.info("Calling entrypoint {}", entrypoint.getClass().getName());
                    entrypoint.initialize();
                });

        FrameworkRegistries.TELEMETRY_COLLECTORS.register(TelemetryFabricModCollector.class,
                new TelemetryFabricModCollector());
        FrameworkRegistries.TELEMETRY_COLLECTORS.register(TelemetryModCollector.class, new TelemetryModCollector());
        FrameworkRegistries.TELEMETRY_COLLECTORS.register(TelemetrySystemCollector.class, new TelemetrySystemCollector());
    }

    private void keybinding(String modId) {
        new FrameworkKeyBinding("key.framework." + modId, -1, FrameworkKeyBinding.MODS, null);
    }

    @EventTarget
    public void onModRegister(ModRegisterEvent event) {
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
        register(new ModAutoSprint());
        register(new ModAntiBreak());
        register(new ModAutoSprintJump());
        register(new ModAutoEat());
        register(new ModAutoTotem());
        register(new ModAutoRespawn());
        register(new ModDeathPositionSaver());
        register(new ModAutoLeave());
        register(new ModTwerk());
        register(new ModHeartView());
        if (config.enableLegacyPiShock) {
            register(new ModPiShockLegacy());
        } else {
            register(new ModPiShock());
        }
        register(new ModOpenShock());
        register(new ModForceLANPort());
        register(new ModLogo());
        register(new ModDiscordChat());
        register(new ModCloudConfig());
        register(new ModGlobalChat());

        FrameworkVersionCheck.performVersionCheck();
    }

    private void extractDefaultProfile(String profileName) throws IOException {
        LOGGER.info("Extracting default profile {}", profileName);

        File profilesDirectory = new File(ConfigManager.BASE_FOLDER, "profiles");
        profilesDirectory.mkdirs();

        File output = new File(profilesDirectory, profileName + ".json");

        try (InputStream inputStream = FrameworkClient.class.getResourceAsStream("/profiles/" + profileName + ".json")) {
            if (inputStream == null) {
                throw new IOException("Profile not found in classpath: " + profileName);
            }

            try (FileOutputStream outputStream = new FileOutputStream(output)) {
                inputStream.transferTo(outputStream);
            }
        }
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

    public ConfigManager getModsManager() {
        return modsManager;
    }
}
