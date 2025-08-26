package de.toxicfox.framework.client.mod.impl;

import de.toxicfox.framework.client.event.EventTarget;
import de.toxicfox.framework.client.event.impl.ClientPlayerTickEvent;
import de.toxicfox.framework.client.mod.Mod;

public class ModAutoSprint extends Mod {
    @Override
    public String getId() {
        return "auto-sprint";
    }

    @Override
    public String getName() {
        return "Auto sprint";
    }

    @EventTarget
    public void onClientPlayerTickEvent(ClientPlayerTickEvent event) {
        assert mc.player != null;
        if (mc.player.horizontalCollision || mc.player.isSneaking() || mc.player.isInFluid() || mc.player.input.getMovementInput().length() <= 1e-5F) {
            return;
        }
        mc.player.setSprinting(true);
    }

    @Override
    public boolean isHacked() {
        return true;
    }
}
