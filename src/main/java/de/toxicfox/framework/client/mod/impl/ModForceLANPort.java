package de.toxicfox.framework.client.mod.impl;

import de.toxicfox.config.auto.Saved;
import de.toxicfox.framework.client.config.Configurable;
import de.toxicfox.framework.client.mod.Mod;

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
