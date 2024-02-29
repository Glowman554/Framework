package de.glowman554.framework.client.command.impl;

import de.glowman554.framework.client.FrameworkClient;
import de.glowman554.framework.client.command.Command;
import de.glowman554.framework.client.command.CommandEvent;
import de.glowman554.framework.client.registry.FrameworkRegistries;

public class SetHackedCommand extends Command {
    public SetHackedCommand() {
        super("Set if the client includes hack client functionality!");
    }

    @Override
    public void execute(CommandEvent event) {
        if (event.args().length != 1) {
            event.commandFail("Command takes exactly 1 argument!");
        } else {
            FrameworkClient.getInstance().getConfig().enableHacks = Boolean.parseBoolean(event.args()[0]);
            FrameworkClient.getInstance().saveConfig();

            FrameworkRegistries.MODS.getRegistry().forEach((aClass, mod) -> mod.setEnabled(mod.isEnabled()));
        }
    }
}
