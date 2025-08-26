package de.toxicfox.framework.client.mod.impl;

import de.toxicfox.config.auto.Saved;
import de.toxicfox.framework.client.config.Configurable;
import de.toxicfox.framework.client.event.EventTarget;
import de.toxicfox.framework.client.event.impl.ClientPlayerTickEvent;
import de.toxicfox.framework.client.mod.Mod;
import net.minecraft.text.Text;

public class ModAutoLeave extends Mod {
    @Saved
    @Configurable(text = "Quit at health")
    private final int health = 10;

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
            mc.world.disconnect(Text.of("Low health"));

            setEnabled(false);
        }
    }

    @Override
    public boolean isHacked() {
        return true;
    }
}
