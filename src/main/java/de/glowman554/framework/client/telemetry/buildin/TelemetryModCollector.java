package de.glowman554.framework.client.telemetry.buildin;

import de.glowman554.framework.client.mod.Mod;
import de.glowman554.framework.client.telemetry.TelemetryCollector;
import net.shadew.json.JsonNode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;

public class TelemetryModCollector implements TelemetryCollector {
    private final ArrayList<Mod> updates = new ArrayList<>();

    @Override
    public JsonNode json() {
        JsonNode packet = JsonNode.object();
        for (Mod mod : updates) {
            packet.set(mod.getId(), mod.toJSON());
        }
        updates.clear();
        return packet;
    }

    @Override
    public String id() {
        return "mod";
    }

    @Override
    public boolean collect() {
        return !updates.isEmpty();
    }

    public void send(Mod mod) {
        if (mod.getClass().isAnnotationPresent(ModTelemetryDisabled.class)) {
            return;
        }
        if (updates.contains(mod)) {
            return;
        }
        updates.add(mod);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface ModTelemetryDisabled {
    }
}
