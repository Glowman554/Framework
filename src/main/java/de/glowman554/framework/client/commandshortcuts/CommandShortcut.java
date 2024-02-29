package de.glowman554.framework.client.commandshortcuts;

import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;
import de.glowman554.framework.client.FrameworkClient;
import de.glowman554.framework.client.command.CommandEvent;
import de.glowman554.framework.client.screen.NewCommandShortcutScreen;
import net.minecraft.client.MinecraftClient;

public class CommandShortcut extends AutoSavable {
    @Saved
    public String name;
    @Saved
    public String command;
    @Saved
    public boolean keybind = false;

    public CommandShortcut() {

    }

    public CommandShortcut(String name, String command) {
        this.name = name;
        this.command = command;
    }

    public boolean execute() {
        if (command.startsWith(FrameworkClient.getInstance().getCommandManager().prefix)) {
            try {
                FrameworkClient.getInstance().getCommandManager().onCommand(CommandEvent.from(command));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (command.equals("@new")) {
            NewCommandShortcutScreen.open();
            return false;
        } else {
            assert MinecraftClient.getInstance().player != null;
            MinecraftClient.getInstance().player.networkHandler.sendChatCommand(command);
        }
        return true;
    }
}
