package de.glowman554.framework.client.commandshortcuts;

import de.glowman554.config.ConfigManager;
import de.glowman554.framework.client.FrameworkKeyBinding;
import org.lwjgl.glfw.GLFW;

public class CommandShortcutsManager {
    private final ConfigManager commandShortcutConfigManager;
    private CommandShortcutConfig commandShortcutConfig = new CommandShortcutConfig();


    public CommandShortcutsManager() {

        commandShortcutConfigManager = new ConfigManager("command_shortcuts", false);
        try {
            commandShortcutConfig = (CommandShortcutConfig) commandShortcutConfigManager.loadValue("shortcuts", commandShortcutConfig);
        } catch (IllegalArgumentException ignored) {
        }
        save();


        for (int i = 0; i < commandShortcutConfig.shortcuts.length; i++) {
            CommandShortcut shortcut = commandShortcutConfig.shortcuts[i];
            if (shortcut.keybind) {
                new FrameworkKeyBinding("key.framework.command." + shortcut.name.toLowerCase().replaceAll(" ", "_"), GLFW.GLFW_KEY_UNKNOWN, "key.framework.commands", shortcut::execute);
            }
        }
    }


    public CommandShortcutConfig getCommandShortcutConfig() {
        return commandShortcutConfig;
    }

    public void save() {
        commandShortcutConfigManager.setValue("shortcuts", commandShortcutConfig);
    }
}
