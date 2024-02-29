package de.glowman554.framework.client.mod.impl;

import de.glowman554.framework.client.mod.Mod;

public class ModFullBright extends Mod {
    @Override
    public String getId() {
        return "fullbright";
    }

    @Override
    public String getName() {
        return "Fullbright";
    }

    @Override
    public boolean isHacked() {
        return false;
    }
}
