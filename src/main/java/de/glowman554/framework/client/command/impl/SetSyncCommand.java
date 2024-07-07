package de.glowman554.framework.client.command.impl;

import de.glowman554.framework.client.FrameworkClient;
import de.glowman554.framework.client.command.Command;
import de.glowman554.framework.client.command.CommandEvent;
import net.minecraft.client.MinecraftClient;

public class SetSyncCommand extends Command {
    public SetSyncCommand() {
        super("Set if the client syncs the config!");
    }

    @Override
    public void execute(CommandEvent event) {
        if (event.args().length != 1) {
            event.commandFail("Command takes exactly 1 argument!");
        } else {
            FrameworkClient.getInstance().getConfig().sync = Boolean.parseBoolean(event.args()[0]);
            FrameworkClient.getInstance().saveConfig();

            MinecraftClient.getInstance().stop();
        }
    }
}
