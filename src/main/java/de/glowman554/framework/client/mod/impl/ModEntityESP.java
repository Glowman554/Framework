package de.glowman554.framework.client.mod.impl;


import de.glowman554.config.auto.Saved;
import de.glowman554.framework.client.config.Configurable;
import de.glowman554.framework.client.mod.Mod;

public class ModEntityESP extends Mod {
    @Saved
    @Configurable(text = "Dangerous Distance")
    private int dangerousDistance = 200;
    @Saved
    @Configurable(text = "Caution Distance")
    private int cautionDistance = 600;

    @Saved
    @Configurable(text = "Colorful ESP")
    boolean colorful = false;

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

    public int getCautionDistance() {
        return cautionDistance;
    }

    public int getDangerousDistance() {
        return dangerousDistance;
    }

    public boolean isColorful() {
        return colorful;
    }
}
