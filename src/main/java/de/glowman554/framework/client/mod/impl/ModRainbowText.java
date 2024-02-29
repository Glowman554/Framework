package de.glowman554.framework.client.mod.impl;


import de.glowman554.framework.client.event.EventTarget;
import de.glowman554.framework.client.event.impl.TickEvent;
import de.glowman554.framework.client.mod.Mod;

public class ModRainbowText extends Mod {
    private float hue = 0f;

    @EventTarget
    public void onTick(TickEvent e) {
        hue += 0.01f;
        if (hue > 1f) {
            hue = 0f;
        }
    }

    public float getHue() {
        return hue;
    }

    @Override
    public String getId() {
        return "rainbow";
    }

    @Override
    public String getName() {
        return "Rainbow";
    }

    @Override
    public boolean isHacked() {
        return false;
    }
}
