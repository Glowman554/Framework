package de.glowman554.framework.client.mod.impl;

import com.google.gson.JsonObject;

import de.glowman554.config.auto.Saved;
import de.glowman554.framework.client.config.Configurable;
import de.glowman554.framework.client.event.EventTarget;
import de.glowman554.framework.client.event.impl.DeathEvent;
import de.glowman554.framework.client.mod.Mod;
import de.glowman554.framework.client.telemetry.buildin.TelemetryModCollector.ModTelemetryDisabled;
import de.glowman554.framework.client.utils.WebClient;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Map;

@ModTelemetryDisabled
public class ModPiShock extends Mod {
    @Saved
    @Configurable(text = "Shock intensity")
    private int duration = 1;
    @Saved
    @Configurable(text = "Shock intensity")
    private int intensity = 50;

    @Saved
    @Configurable(text = "PiShock username")
    private String username = "";
    @Saved
    @Configurable(text = "PiShock api key")
    private String apikey = "";
    @Saved
    @Configurable(text = "PiShock share-code")
    private String code = "";
    @Saved
    @Configurable(text = "Application name")
    private String name = "Framework";

    @Override
    public String getId() {
        return "pi-shock";
    }

    @Override
    public String getName() {
        return "PiShock";
    }

    @Override
    public boolean isHacked() {
        return false;
    }

    @EventTarget
    public void onDeath(DeathEvent event) {
        new Thread(() -> {
            JsonNode root = JsonNode.object();
            root.set("Username", username);
            root.set("Name", name);
            root.set("Code", code);
            root.set("Intensity", String.valueOf(intensity));
            root.set("Duration", String.valueOf(duration));
            root.set("Apikey", apikey);
            root.set("Op", String.valueOf(Operation.Shock.op));

            try {
                WebClient.post("https://do.pishock.com/api/apioperate/", Json.json().serialize(root), Map.of("Content-Type", "application/json"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static enum Operation {
        Shock(0), Vibrate(1), Beep(2);

        private Operation(int op) {
            this.op = op;
        }

        private final int op;
    }

}
