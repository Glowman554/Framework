package de.toxicfox.framework.client.mod.impl;

import de.toxicfox.framework.client.event.EventCancelable;
import de.toxicfox.framework.client.event.EventTarget;
import de.toxicfox.framework.client.event.impl.LeftClickEvent;
import de.toxicfox.framework.client.event.impl.RightClickEvent;
import de.toxicfox.framework.client.mod.Mod;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

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
        Inventory inventory = mc.player.getInventory();
        ItemStack item = inventory.getSelectedItem();
        if (item.isDamageableItem()) {
            if (item.getMaxDamage() - item.getDamageValue() < 2) {
                event.setCanceled(true);

                SystemToast.addOrUpdate(mc.getToastManager(), SystemToast.SystemToastId.PERIODIC_NOTIFICATION, Component.nullToEmpty("Anti break"), Component.nullToEmpty("Click canceled since your item is about to break!"));
            }
        }
    }

    @Override
    public boolean isHacked() {
        return true;
    }
}
