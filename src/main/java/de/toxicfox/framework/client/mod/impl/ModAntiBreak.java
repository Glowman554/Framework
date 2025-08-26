package de.toxicfox.framework.client.mod.impl;

import de.toxicfox.framework.client.event.EventCancelable;
import de.toxicfox.framework.client.event.EventTarget;
import de.toxicfox.framework.client.event.impl.LeftClickEvent;
import de.toxicfox.framework.client.event.impl.RightClickEvent;
import de.toxicfox.framework.client.mod.Mod;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class ModAntiBreak extends Mod {
    @Override
    public String getId() {
        return "anti-break";
    }

    @Override
    public String getName() {
        return "Anti break";
    }

    @EventTarget
    public void onLeftClickEvent(LeftClickEvent event) {
        doAntiBreak(event);
    }

    @EventTarget
    public void onRightClick(RightClickEvent event) {
        doAntiBreak(event);
    }

    private void doAntiBreak(EventCancelable event) {
        assert mc.player != null;
        PlayerInventory inventory = mc.player.getInventory();
        ItemStack item = inventory.getSelectedStack();
        if (item.isDamageable()) {
            if (item.getMaxDamage() - item.getDamage() < 2) {
                event.setCanceled(true);

                SystemToast.show(mc.getToastManager(), SystemToast.Type.PERIODIC_NOTIFICATION, Text.of("Anti break"), Text.of("Click canceled since your item is about to break!"));
            }
        }
    }

    @Override
    public boolean isHacked() {
        return true;
    }
}
