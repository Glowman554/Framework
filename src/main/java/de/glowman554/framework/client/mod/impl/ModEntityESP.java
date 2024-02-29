package de.glowman554.framework.client.mod.impl;


import de.glowman554.framework.client.mod.Mod;

public class ModEntityESP extends Mod {
    @Override
    public String getId() {
        return "entity-esp";
    }

    @Override
    public String getName() {
        return "Entity ESP";
    }

    @Override
    public void setEnabled(boolean newEnabled) {
        super.setEnabled(newEnabled);
    }

    @Override
    public boolean isHacked() {
        return true;
    }
}
