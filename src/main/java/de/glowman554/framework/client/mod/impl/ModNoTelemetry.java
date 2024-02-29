package de.glowman554.framework.client.mod.impl;

import de.glowman554.framework.client.mod.Mod;

public class ModNoTelemetry extends Mod {
    @Override
    public String getId() {
        return "no-telemetry";
    }

    @Override
    public String getName() {
        return "No telemetry";
    }

    @Override
    public boolean defaultEnable() {
        return true;
    }

    @Override
    public boolean isHacked() {
        return false;
    }
}
