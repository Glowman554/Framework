package de.glowman554.framework.client.telemetry.buildin;

import de.glowman554.framework.client.telemetry.TelemetryCollector;
import net.arikia.dev.drpc.DiscordUser;
import net.shadew.json.JsonNode;

public class TelemetryDiscordUserCollector implements TelemetryCollector {
    private DiscordUser user = null;

    @Override
    public JsonNode json() {
        JsonNode obj = JsonNode.object();
        obj.set("userId", user.userId);
        obj.set("username", user.username);
        obj.set("discriminator", user.discriminator);
        obj.set("avatar", user.avatar);
        user = null;
        return obj;
    }

    @Override
    public String id() {
        return "discord";
    }

    @Override
    public boolean collect() {
        return user != null;
    }

    public void setUser(DiscordUser user) {
        this.user = user;
    }
}
