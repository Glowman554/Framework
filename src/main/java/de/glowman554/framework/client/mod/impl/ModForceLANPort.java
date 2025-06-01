package de.glowman554.framework.client.mod.impl;

import de.glowman554.config.auto.Saved;
import de.glowman554.framework.client.config.Configurable;
import de.glowman554.framework.client.mod.Mod;

public class ModForceLANPort extends Mod {
    @Saved
    @Configurable(text = "LAN Port")
    private int port = 25565;

    @Override
    public String getId() {
        return "force-lan-port";
    }

    @Override
    public String getName() {
        return "Force LAN Port";
    }

    @Override
    public boolean isHacked() {
        return false;
    }

    public int getPort() {
        return port;
    }
}
