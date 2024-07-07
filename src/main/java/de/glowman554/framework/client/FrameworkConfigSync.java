package de.glowman554.framework.client;

import de.glowman554.framework.client.mod.Mod;
import de.glowman554.framework.client.registry.FrameworkRegistries;
import de.glowman554.framework.client.utils.WebClient;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

import java.io.IOException;
import java.util.Map;

public class FrameworkConfigSync {
    private final String authenticationToken;

    public FrameworkConfigSync(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

    public void sync() {
        JsonNode root = JsonNode.object();

        for (Mod mod : FrameworkRegistries.MODS.getRegistry().values()) {
            root.set(mod.getId(), mod.toJSON());
        }

        try {
            String result = WebClient.post(FrameworkClient.getInstance().getConfig().development.configSync, Json.json().serialize(root), Map.of("Authentication", authenticationToken));
            FrameworkClient.LOGGER.info("config sync: {}", result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            String result = WebClient.get(FrameworkClient.getInstance().getConfig().development.configSync, Map.of("Authentication", authenticationToken));
            JsonNode root = Json.json().parse(result);
            for (Mod mod : FrameworkRegistries.MODS.getRegistry().values()) {
                try {
                    mod.fromJSON(root.get(mod.getId()));
                    mod.setEnabled(mod.isEnabled()); // trigger reload
                } catch (IllegalArgumentException e) {
                    mod.setEnabled(false);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean ok(String token) {
        try {
            String result = WebClient.get(FrameworkClient.getInstance().getConfig().development.configTest, Map.of("Authentication", token));
            JsonNode root = Json.json().parse(result);

            if (root.get("error") != null) {
                return false;
            }

            FrameworkClient.LOGGER.info("cloud: {}", root.get("username").asString());
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
