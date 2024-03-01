package de.glowman554.framework.client.mod.impl;

import de.glowman554.config.auto.Saved;
import de.glowman554.framework.client.config.Configurable;
import de.glowman554.framework.client.event.EventTarget;
import de.glowman554.framework.client.event.impl.ClientPlayerTickEvent;
import de.glowman554.framework.client.mod.Mod;

public class ModAutoLeave extends Mod {
    @Saved
    @Configurable(text = "Quit at health")
    private int health = 10;

    @Override
    public String getId() {
        return "auto-leave";
    }

    @Override
    public String getName() {
        return "Auto leave";
    }

    @EventTarget
    public void onClientPlayerTick(ClientPlayerTickEvent event) {
        assert mc.player != null;
        if (mc.player.getAbilities().creativeMode) {
            return;
        }

        float currentHealth = mc.player.getHealth();
        if (currentHealth <= health) {
            assert mc.world != null;
            mc.world.disconnect();

            setEnabled(false);
        }
    }

    @Override
    public boolean isHacked() {
        return true;
    }
}
