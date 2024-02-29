package de.glowman554.framework.client.telemetry;

import net.shadew.json.JsonNode;

public interface TelemetryCollector {
    JsonNode json();

    String id();

    boolean collect();
}
