package de.toxicfox.framework.client.mod.impl;

import de.toxicfox.config.auto.Saved;
import de.toxicfox.framework.client.FrameworkClient;
import de.toxicfox.framework.client.command.impl.ShockCommand;
import de.toxicfox.framework.client.config.Configurable;
import de.toxicfox.framework.client.event.EventTarget;
import de.toxicfox.framework.client.event.impl.DeathEvent;
import de.toxicfox.framework.client.mod.Mod;
import de.toxicfox.framework.client.screen.TestButtonExecutor;
import de.toxicfox.framework.client.telemetry.buildin.TelemetryModCollector;
import de.toxicfox.framework.client.utils.WebClient;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@TelemetryModCollector.Disabled
public class ModOpenShock extends Mod {
    @Saved
    @Configurable(text = "OpenShock token")
    private String token = "";

    @Saved
    @Configurable(text = "Shock duration")
    private int duration = 30;
    @Saved
    @Configurable(text = "Shock intensity")
    private int intensity = 100;

    @Configurable(text = "Test connection")
    private TestButtonExecutor testButton = this::trigger;

    private final ArrayList<Shocker> shockers = new ArrayList<>();

    @Override
    public void setEnabled(boolean newEnabled) {
        super.setEnabled(newEnabled);
        shockers.clear();

        if (isEnabled()) {
            loadShockers();
            FrameworkClient.getInstance().getCommandManager().addCommand("openshock-shock", new ShockCommand(this::trigger));
        } else {
            FrameworkClient.getInstance().getCommandManager().removeCommand("openshock-shock");

        }
    }

    private void loadShockers() {
        try {
            JsonNode response = Json.json().parse(WebClient.get("https://api.openshock.app/1/shockers/own", Map.of("Open-Shock-Token", token)));
            for (JsonNode dataNode : response.get("data")) {
                for (JsonNode shockerNode : dataNode.get("shockers")) {
                    Shocker shocker = new Shocker(shockerNode.get("name").asString(), shockerNode.get("id").asString());
                    shockers.add(shocker);
                    FrameworkClient.LOGGER.info("shocker detected: {}", shocker);
                }
            }
        } catch (IOException e) {
            FrameworkClient.LOGGER.error("Could not load shocker list: {}", e.toString());
        }
    }

    @EventTarget
    public void onDeath(DeathEvent event) {
        new Thread(this::trigger).start();
    }

    private void trigger() {
        JsonNode root = JsonNode.object();

        JsonNode shocks = JsonNode.array();
        for (Shocker shocker : shockers) {
            JsonNode shock = JsonNode.object();
            shock.set("id", shocker.id);
            shock.set("type", "Shock");
            shock.set("intensity", intensity);
            shock.set("duration", duration * 1000);
            shock.set("exclusive", false);
            shocks.add(shock);
        }

        root.set("shocks", shocks);
        root.set("customName", "FrameworkClient");

        try {
            WebClient.post("https://api.openshock.app/2/shockers/control", Json.json().serialize(root), Map.of("Content-Type", "application/json", "Open-Shock-Token", token));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getId() {
        return "openshock";
    }

    @Override
    public String getName() {
        return "OpenShock";
    }

    @Override
    public boolean isHacked() {
        return false;
    }

    private record Shocker(String displayName, String id) {
    }
}
