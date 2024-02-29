package de.glowman554.framework.client.mod.impl;

import de.glowman554.framework.client.event.EventTarget;
import de.glowman554.framework.client.event.impl.LeftClickEvent;
import de.glowman554.framework.client.mod.Mod;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.hit.HitResult;

public class ModCriticals extends Mod {

    @Override
    public String getId() {
        return "criticals";
    }

    @Override
    public String getName() {
        return "Criticals";
    }

    @EventTarget
    public void onLeftClick(LeftClickEvent event) {
        if (mc.crosshairTarget == null || mc.crosshairTarget.getType() != HitResult.Type.ENTITY) {
            return;
        }
        assert mc.player != null;
        if (mc.player.isOnGround() && !mc.player.isInFluid()) {
            double posX = mc.player.getX();
            double posY = mc.player.getY();
            double posZ = mc.player.getZ();

            sendPositionPacket(posX, posY + 0.0625D, posZ, true);
            sendPositionPacket(posX, posY, posZ, false);
            sendPositionPacket(posX, posY + 1.1E-5D, posZ, false);
            sendPositionPacket(posX, posY, posZ, false);
        }
    }

    private void sendPositionPacket(double x, double y, double z, boolean onGround) {
        assert mc.player != null;
        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, onGround));
    }
}
