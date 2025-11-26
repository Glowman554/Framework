package de.toxicfox.framework.client.mod.impl;

import de.toxicfox.config.auto.Saved;
import de.toxicfox.framework.client.FrameworkClient;
import de.toxicfox.framework.client.command.Command;
import de.toxicfox.framework.client.command.CommandEvent;
import de.toxicfox.framework.client.config.Configurable;
import de.toxicfox.framework.client.event.EventTarget;
import de.toxicfox.framework.client.event.impl.ClientFinishLoadingEvent;
import de.toxicfox.framework.client.event.impl.ClientStopEvent;
import de.toxicfox.framework.client.mod.Mod;
import de.toxicfox.framework.client.registry.FrameworkRegistries;
import de.toxicfox.framework.client.utils.WebClient;
import net.minecraft.client.MinecraftClient;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

import java.io.IOException;
import java.util.Map;

public class ModCloudConfig extends Mod {
    private final Json json = Json.json();
    private String token;

    @Saved
    @Configurable(text = "Automatically upload / download config")
    private boolean automaticSync = false;

    @Override
    public String getId() {
        return "cloud-config";
    }

    @Override
    public String getName() {
        return "Cloud config";
    }

    @Override
    public boolean isHacked() {
        return false;
    }

    @Override
    public void setEnabled(boolean newEnabled) {
        super.setEnabled(newEnabled);

        if (isEnabled()) {
            token = MinecraftClient.getInstance().getSession().getAccessToken();
            FrameworkClient.getInstance().getCommandManager().addCommand("config", new ConfigCommand());
        } else {
            token = null;
            FrameworkClient.getInstance().getCommandManager().removeCommand("config");
        }
    }

    @EventTarget
    public void onClientFinishLoading(ClientFinishLoadingEvent event) {
        try {
            if (ready() && automaticSync) {
                JsonNode config = download("_current");
                loadConfig(config);
            }
        } catch (Exception e) {
            FrameworkClient.LOGGER.error("Could not load cloud config", e);
        }
    }

    @EventTarget
    public void onClientStop(ClientStopEvent event) {
        if (ready() && automaticSync) {
            upload("_current", createConfig());
        }
    }

    public JsonNode createConfig() {
        JsonNode config = JsonNode.object();

        for (Mod mod : FrameworkRegistries.MODS.getRegistry().values()) {
            config.set(mod.getId(), mod.toJSON());
        }

        return config;
    }

    public void loadConfig(JsonNode config) {
        for (Mod mod : FrameworkRegistries.MODS.getRegistry().values()) {
            try {
                mod.fromJSON(config.get(mod.getId()));
                mod.setEnabled(mod.isEnabled()); // trigger reload
            } catch (Exception e) {
                mod.setEnabled(false);
            }
        }
    }

    public void upload(String profile, JsonNode config) {
        JsonNode root = JsonNode.object();
        root.set("name", profile);
        root.set("configuration", config);

        try {
            String result = WebClient.post(FrameworkClient.getInstance().getConfig().development.backend.profileUpload, json.serialize(root), Map.of("Authentication", token));
            FrameworkClient.LOGGER.info("profile {} upload: {}", profile, result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JsonNode download(String profile) {
        JsonNode req = JsonNode.object();
        req.set("name", profile);

        try {
            String result = WebClient.post(FrameworkClient.getInstance().getConfig().development.backend.profileDownload, json.serialize(req), Map.of("Authentication", token));
            return json.parse(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean ready() {
        if (token == null) {
            return false;
        }

        try {
            String result = WebClient.get(FrameworkClient.getInstance().getConfig().development.backend.testToken, Map.of("Authentication", token));
            JsonNode root = json.parse(result);

            FrameworkClient.LOGGER.info("profile user: {}", root.get("name").asString());
        } catch (IOException e) {
            return false;
        }
        return true;
    }

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
                        if (ready() && automaticSync) {
                            upload("_current", createConfig());
                        }
                        event.commandSuccess("Upload successful");
                    }
                    break;

                    case "download": {
                        event.commandSuccess("Downloading...");
                        JsonNode config = download("_current");
                        loadConfig(config);
                        event.commandSuccess("Download successful");
                    }
                    break;
                }
            }
        }
    }

}
