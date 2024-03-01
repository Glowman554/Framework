package de.glowman554.framework.client.registry;

import de.glowman554.config.ConfigManager;
import de.glowman554.framework.client.FrameworkClient;
import de.glowman554.framework.client.FrameworkKeyBinding;
import de.glowman554.framework.client.mod.Mod;
import de.glowman554.framework.client.mod.ModDraggable;
import de.glowman554.framework.client.telemetry.TelemetryCollector;

public class FrameworkRegistries {
    public static final FrameworkRegistry<String, FrameworkKeyBinding> KEY_BINDINGS = new FrameworkRegistry<>((s, frameworkKeyBinding) -> {
    });
    public static final FrameworkRegistry<Class<? extends TelemetryCollector>, TelemetryCollector> TELEMETRY_COLLECTORS = new FrameworkRegistry<>((aClass, telemetryCollector) -> {
    });
    public static final FrameworkRegistry<Class<? extends Mod>, Mod> MODS = new FrameworkRegistry<>((aClass, mod) -> {
        if (mod instanceof ModDraggable modDraggable) {
            FrameworkClient.getInstance().getHudManager().register(modDraggable);
        }

        String keybindingIdentifier = "key.framework." + mod.getId();
        if (KEY_BINDINGS.has(keybindingIdentifier)) {
            FrameworkKeyBinding binding = KEY_BINDINGS.get(keybindingIdentifier);
            FrameworkClient.LOGGER.info("Binding keybinding {} to mod {}", binding.getTranslationKey(), mod.getId());
            binding.setLambda(mod::onKeybinding);
        }
        if (FrameworkClient.getInstance().getConfig().development.singleModFile) {
            mod.configure(FrameworkClient.getInstance().getModsManager());
        } else {
            mod.configure(new ConfigManager("mods/" + mod.getId(), false));
        }
        FrameworkClient.LOGGER.info("Registered mod {} with status {}", mod.getId(), mod.isEnabled() ? "enabled" : "disabled");
    });
}
