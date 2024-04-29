package de.glowman554.framework.client.mod.impl;

import de.glowman554.config.auto.Saved;
import de.glowman554.framework.client.config.Configurable;
import de.glowman554.framework.client.mod.Mod;

public class ModMenuBackground extends Mod {
    @Saved
    @Configurable(text = "Background image")
    private final String background = "textures/gui/options_background.png";

    @Override
    public String getId() {
        return "menu-background";
    }

    @Override
    public String getName() {
        return "Menu background";
    }

    @Override
    public boolean isHacked() {
        return false;
    }

    @Override
    public boolean defaultEnable() {
        return true;
    }

    public String getBackground() {
        return background;
    }
}
