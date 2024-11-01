package de.glowman554.framework.client.command.impl;

import de.glowman554.framework.client.FrameworkClient;
import de.glowman554.framework.client.command.Command;
import de.glowman554.framework.client.command.CommandEvent;

public class ConfigCommand extends Command {
    public ConfigCommand() {
        super("Upload / Download the configuration");
    }

    @Override
    public void execute(CommandEvent event) {
        if (event.args().length != 1) {
            event.commandFail("Command takes exactly 1 argument!");
            event.commandFail("Usage: config <upload/download>");
        } else {
            switch (event.args()[0]) {
                case "upload": {
                    event.commandSuccess("Uploading...");
                    FrameworkClient.getInstance().getConfigSync().upload();
                    event.commandSuccess("Upload successful");
                }
                break;

                case "download": {
                    event.commandSuccess("Downloading...");
                    FrameworkClient.getInstance().getConfigSync().download();
                    event.commandSuccess("Download successful");
                }
                break;
            }
        }
    }
}
