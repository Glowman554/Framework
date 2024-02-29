package de.glowman554.framework.client.mod.impl;

import de.glowman554.framework.client.event.EventTarget;
import de.glowman554.framework.client.event.impl.DeathEvent;
import de.glowman554.framework.client.mod.Mod;

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
        mc.player.requestRespawn();
    }
}
