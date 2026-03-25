package de.toxicfox.framework.client;

import de.toxicfox.framework.client.utils.WebClient;
import net.minecraft.client.multiplayer.ServerData;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

import java.util.ArrayList;
import java.util.Map;

public class ServerInfoFeatured extends ServerData {
    private static final ArrayList<ServerInfoFeatured> featuredServers = new ArrayList<>();


    public ServerInfoFeatured(String name, String address) {
        super(name, address, Type.OTHER);
    }

    public static void load(String from) {
        try {
            String result = WebClient.get(from, Map.of("Accept", "application/json"));

            JsonNode root = Json.json().parse(result);
            for (JsonNode entry : root) {
                featuredServers.add(new ServerInfoFeatured(entry.get("name").asString(), entry.get("address").asString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ServerInfoFeatured> getFeaturedServers() {
        return featuredServers;
    }
}
