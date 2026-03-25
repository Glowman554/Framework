package de.toxicfox.framework.client.mod.impl;

import de.toxicfox.framework.client.event.EventTarget;
import de.toxicfox.framework.client.event.impl.DeathEvent;
import de.toxicfox.framework.client.mod.Mod;

public class ModAutoRespawn extends Mod {
    @Override
    public String getId() {
        return "auto-respawn";
    }

    @Override
    public String getName() {
        return "Auto respawn";
    }

    @EventTarget
    public void onDeathEvent(DeathEvent event) {
        assert mc.player != null;
        mc.player.respawn();
    }

    @Override
    public boolean isHacked() {
        return true;
    }
}
