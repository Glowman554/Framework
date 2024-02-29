package de.glowman554.framework.client;

import de.glowman554.framework.client.utils.WebClient;
import net.minecraft.client.network.ServerInfo;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerInfoFeatured extends ServerInfo {
    private static final ArrayList<ServerInfoFeatured> featuredServers = new ArrayList<>();


    public ServerInfoFeatured(String name, String address) {
        super(name, address, ServerType.OTHER);
    }

    public static void load(String from) {
        try {
            String result = WebClient.get(from, new HashMap<>());

            JsonNode root = Json.json().parse(result);
            for (JsonNode entry : root) {
                featuredServers.add(new ServerInfoFeatured(entry.get("name").asString(), entry.get("address").asString()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ServerInfoFeatured> getFeaturedServers() {
        return featuredServers;
    }
}
