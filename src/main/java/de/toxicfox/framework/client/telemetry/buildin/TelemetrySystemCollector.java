package de.toxicfox.framework.client.telemetry.buildin;

import de.toxicfox.framework.client.telemetry.TelemetryCollector;
import net.shadew.json.JsonNode;

public class TelemetrySystemCollector implements TelemetryCollector {
    private boolean collected = false;

    @Override
    public JsonNode json() {
        JsonNode root = JsonNode.object();

        root.set("osName", System.getProperty("os.name"));
        root.set("osVersion", System.getProperty("os.version"));
        root.set("osArch", System.getProperty("os.arch"));
        root.set("cpuCores", Runtime.getRuntime().availableProcessors());

        return root;
    }

    @Override
    public String id() {
        return "system";
    }

    @Override
    public boolean collect() {
        if (!collected) {
            collected = true;
            return true;
        }
        return false;
    }
}
