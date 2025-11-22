package de.toxicfox.framework.client.registry;

import de.toxicfox.config.ConfigManager;
import de.toxicfox.framework.client.FrameworkClient;
import de.toxicfox.framework.client.FrameworkKeyBinding;
import de.toxicfox.framework.client.mod.Mod;
import de.toxicfox.framework.client.mod.ModDraggable;
import de.toxicfox.framework.client.telemetry.TelemetryCollector;

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
            FrameworkClient.LOGGER.info("Binding keybinding {} to mod {}", binding.getBoundKeyTranslationKey(), mod.getId());
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
