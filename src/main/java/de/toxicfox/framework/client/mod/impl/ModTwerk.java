package de.toxicfox.framework.client.mod.impl;

import de.toxicfox.config.auto.Saved;
import de.toxicfox.framework.client.config.Configurable;
import de.toxicfox.framework.client.event.EventTarget;
import de.toxicfox.framework.client.event.impl.ClientPlayerTickEvent;
import de.toxicfox.framework.client.mod.Mod;
import net.minecraft.client.KeyMapping;

public class ModTwerk extends Mod {
    @Saved
    @Configurable(text = "Twerk delay")
    private final int delay = 1;
    private int timer;

    @Override
    public String getId() {
        return "twerk";
    }

    @Override
    public String getName() {
        return "Twerk";
    }

    @EventTarget
    public void onClientTick(ClientPlayerTickEvent event) {
        timer++;
        if (timer < delay) {
            return;
        }

        KeyMapping sneakKey = mc.options.keyShift;
        sneakKey.setDown(!sneakKey.isDown());
        timer = -1;
    }

    @Override
    public boolean isHacked() {
        return false;
    }
}
