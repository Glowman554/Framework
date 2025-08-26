package de.toxicfox.framework.client.command.impl;

import de.toxicfox.config.ConfigManager;
import de.toxicfox.framework.client.FrameworkClient;
import de.toxicfox.framework.client.command.Command;
import de.toxicfox.framework.client.command.CommandEvent;
import de.toxicfox.framework.client.mod.Mod;
import de.toxicfox.framework.client.registry.FrameworkRegistries;

import java.io.File;

public class ProfileCommand extends Command {
    public ProfileCommand() {
        super("Load / save profiles!");
    }

    @Override
    public void execute(CommandEvent event) {
        if (event.args().length != 2) {
            event.commandFail("Command takes exactly 2 arguments!");
            event.commandFail("Usage: profile <load/save> <id>");
        } else {
            switch (event.args()[0]) {
                case "load": {
                    try {
                        ConfigManager profileConfig = new ConfigManager("profiles/" + event.args()[1], true);

                        for (Mod mod : FrameworkRegistries.MODS.getRegistry().values()) {
                            try {
                                profileConfig.loadValue(mod.getId(), mod);
                                mod.setEnabled(mod.isEnabled()); // trigger reload
                            } catch (IllegalArgumentException e) {
                                mod.setEnabled(false);
                            }
                        }
                        event.commandSuccess("Loaded profile " + event.args()[1]);
                    } catch (IllegalArgumentException e) {
                        event.commandFail("Could not load profile " + event.args()[1]);
                    }
                }
                break;

                case "save": {
                    ConfigManager profileConfig = new ConfigManager("profiles/" + event.args()[1], false);
                    for (Mod mod : FrameworkRegistries.MODS.getRegistry().values()) {
                        profileConfig.setValue(mod.getId(), mod);
                    }
                    event.commandSuccess("Saved profile " + event.args()[1]);
                }
                break;
            }
        }
    }

    @Override
    public void register() {
        super.register();

        File profileFolder = new File(ConfigManager.BASE_FOLDER, "profiles");
        if (!profileFolder.exists()) {
            FrameworkClient.LOGGER.info("Creating data directory {}", profileFolder.getPath());
            profileFolder.mkdir();
        }
    }
}