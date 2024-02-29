package de.glowman554.framework.client.mod.impl;

import de.glowman554.framework.client.event.EventTarget;
import de.glowman554.framework.client.event.impl.ClientPlayerTickEvent;
import de.glowman554.framework.client.mod.Mod;

public class ModAutoSprintJump extends Mod {
    @Override
    public String getId() {
        return "auto-sprint-jump";
    }

    @Override
    public String getName() {
        return "Auto sprint jump";
    }

    @EventTarget
    public void onClientPlayerTickEvent(ClientPlayerTickEvent event) {
        assert mc.player != null;
        if (mc.player.horizontalCollision || mc.player.isSneaking() || mc.player.isInFluid() || mc.player.input.getMovementInput().length() <= 1e-5F) {
            return;
        }
        mc.player.setSprinting(true);
        if (mc.player.isOnGround()) {
            mc.player.jump();
        }
    }

    @Override
    public boolean isHacked() {
        return true;
    }
}
