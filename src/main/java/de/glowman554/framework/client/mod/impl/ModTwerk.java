package de.glowman554.framework.client.mod.impl;

import de.glowman554.config.auto.Saved;
import de.glowman554.framework.client.config.Configurable;
import de.glowman554.framework.client.event.EventTarget;
import de.glowman554.framework.client.event.impl.ClientPlayerTickEvent;
import de.glowman554.framework.client.mod.Mod;
import net.minecraft.client.option.KeyBinding;

public class ModTwerk extends Mod {
    private int timer;

    @Saved
    @Configurable(text = "Twerk delay")
    private final int delay = 1;

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

        KeyBinding sneakKey = mc.options.sneakKey;
        sneakKey.setPressed(!sneakKey.isPressed());
        timer = -1;
    }

    @Override
    public boolean isHacked() {
        return false;
    }
}
