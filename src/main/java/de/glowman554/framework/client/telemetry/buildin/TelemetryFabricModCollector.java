package de.glowman554.framework.client.telemetry.buildin;

import de.glowman554.framework.client.telemetry.TelemetryCollector;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.shadew.json.JsonNode;

import java.time.Instant;


public class TelemetryFabricModCollector implements TelemetryCollector {
    private boolean collected = false;

    @Override
    public JsonNode json() {
        JsonNode result = JsonNode.array();
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            JsonNode obj = JsonNode.object();
            ModMetadata metadata = mod.getMetadata();
            obj.set("name", metadata.getName());
            obj.set("version", metadata.getVersion().getFriendlyString());
            obj.set("description", metadata.getDescription());
            obj.set("type", metadata.getType());
            result.add(obj);
        }

        JsonNode finalResult = JsonNode.object();
        finalResult.set("mods", result);
        finalResult.set("time", Instant.now().toEpochMilli());

        return finalResult;
    }

    @Override
    public String id() {
        return "fabric_mod";
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
