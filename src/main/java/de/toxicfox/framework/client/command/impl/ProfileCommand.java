package de.toxicfox.framework.client.command.impl;

import de.toxicfox.config.ConfigManager;
import de.toxicfox.framework.client.FrameworkClient;
import de.toxicfox.framework.client.command.Command;
import de.toxicfox.framework.client.command.CommandEvent;
import de.toxicfox.framework.client.mod.Mod;
import de.toxicfox.framework.client.mod.impl.ModCloudConfig;
import de.toxicfox.framework.client.registry.FrameworkRegistries;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

import java.io.File;
import java.io.IOException;

public class ProfileCommand extends Command {
    public ProfileCommand() {
        super("Load / save profiles!");
    }

    private JsonNode loadFile(String path) {
        try {
            return Json.json().parse(new File(path));
        } catch (IOException e) {
            return null;
        }
    }

    private void saveFile(String path, JsonNode data) {
        try {
            Json.json().serialize(data, new File(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not save profile " + path, e);
        }
    }

    @Override
    public void execute(CommandEvent event) {
        if (event.args().length != 2) {
            event.commandFail("Command takes exactly 2 arguments!");
            event.commandFail("Usage: profile <load/save> <id>");
        } else {

            ModCloudConfig cc = (ModCloudConfig) FrameworkRegistries.MODS.getRegistry().get(ModCloudConfig.class);

            switch (event.args()[0]) {
                case "load": {
                    try {
                        JsonNode node = loadFile(ConfigManager.BASE_FOLDER + "/profiles/" + event.args()[1] + ".json");
                        if (node == null) {
                            if (cc.isEnabled()) {
                                node = cc.download(event.args()[1]);
                                event.commandSuccess("Downloaded profile from cloud");
                            } else {
                                throw new IllegalArgumentException("Profile does not exist");
                            }
                        }

                        for (Mod mod : FrameworkRegistries.MODS.getRegistry().values()) {
                            try {
                                JsonNode modNode = node.get(mod.getId());
                                if (modNode != null) {
                                    mod.fromJSON(modNode);
                                    mod.setEnabled(mod.isEnabled()); // trigger reload
                                }
                            } catch (IllegalArgumentException e) {
                                mod.setEnabled(false);
                            }
                        }
                        event.commandSuccess("Loaded profile " + event.args()[1]);
                    } catch (IllegalArgumentException | IllegalStateException e) {
                        event.commandFail("Could not load profile " + event.args()[1]);
                    }
                }
                break;

                case "save": {
                    JsonNode config = JsonNode.object();

                    for (Mod mod : FrameworkRegistries.MODS.getRegistry().values()) {
                        config.set(mod.getId(), mod.toJSON());
                    }

                    saveFile(ConfigManager.BASE_FOLDER + "/profiles/" + event.args()[1] + ".json", config);
                    if (cc.isEnabled()) {
                        cc.upload(event.args()[1], config);
                        event.commandSuccess("Uploaded profile to cloud");
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