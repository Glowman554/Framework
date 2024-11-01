package de.glowman554.framework.client.command.impl;

import de.glowman554.framework.client.command.Command;
import de.glowman554.framework.client.command.CommandEvent;

public class ShockCommand extends Command {
    private final Shocker shocker;

    public ShockCommand(Shocker shocker) {
        super("Send a shock signal");
        this.shocker = shocker;
    }

    @Override
    public void execute(CommandEvent event) {
        if (event.args().length != 0) {
            event.commandFail("Command takes no arguments!");
        } else {
            event.commandSuccess("Sending trigger...");
            shocker.trigger();
        }
    }

    public interface Shocker {
        void trigger();
    }
}
