package de.toxicfox.framework.client.command.impl;

import de.toxicfox.framework.client.FrameworkClient;
import de.toxicfox.framework.client.command.Command;
import de.toxicfox.framework.client.command.CommandEvent;
import de.toxicfox.framework.client.registry.FrameworkRegistries;

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
