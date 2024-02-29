package de.glowman554.framework.client.mod.impl;

import de.glowman554.framework.client.event.EventTarget;
import de.glowman554.framework.client.event.impl.ClientPlayerTickEvent;
import de.glowman554.framework.client.hud.ScreenPosition;
import de.glowman554.framework.client.mod.ModDraggable;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class ModAutoTotem extends ModDraggable {
    private int totemsLeft;
    private int nextTickSlot;

    @Override
    public int getWidth() {
        return textRenderer.getWidth("11 totems left");
    }

    @Override
    public int getHeight() {
        return textRenderer.fontHeight;
    }

    @Override
    public void render(DrawContext drawContext, ScreenPosition pos) {
        drawContext.drawText(textRenderer, totemsLeft + " totems left", pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, -1, true);
    }

    @Override
    public void renderDummy(DrawContext drawContext, ScreenPosition pos) {
        drawContext.drawText(textRenderer, "11 totems left", pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, -1, true);
    }

    @Override
    public String getId() {
        return "auto-totem";
    }

    @Override
    public String getName() {
        return "Auto totem";
    }

    @Override
    public void setEnabled(boolean newEnabled) {
        super.setEnabled(newEnabled);
        reset();
    }

    private void reset() {
        totemsLeft = 0;
        nextTickSlot = -1;
    }

    @EventTarget
    public void onClientPlayerTickEvent(ClientPlayerTickEvent event) {
        finishMovingTotem();

        assert mc.player != null;

        PlayerInventory inventory = mc.player.getInventory();

        int nextTotemSlot = searchForTotems(mc.player.getInventory());
        ItemStack offhandStack = inventory.getStack(40);
        if (offhandStack.getItem() == Items.TOTEM_OF_UNDYING) {
            totemsLeft++;
            return;
        }


        if ((mc.currentScreen instanceof HandledScreen<?> && !(mc.currentScreen instanceof AbstractInventoryScreen<?>)) || nextTotemSlot == -1) {
            return;
        }

        moveTotem(nextTotemSlot, offhandStack);
    }

    private void moveTotem(int nextTotemSlot, ItemStack offhandStack) {
        boolean offhandEmpty = offhandStack.isEmpty();

        assert mc.interactionManager != null;
        mc.interactionManager.clickSlot(0, nextTotemSlot, 0, SlotActionType.PICKUP, mc.player);
        mc.interactionManager.clickSlot(0, 45, 0, SlotActionType.PICKUP, mc.player);


        if (!offhandEmpty) {
            nextTickSlot = nextTotemSlot;
        }
    }

    private void finishMovingTotem() {
        if (nextTickSlot == -1) {
            return;
        }

        assert mc.interactionManager != null;
        mc.interactionManager.clickSlot(0, nextTickSlot, 0, SlotActionType.PICKUP, mc.player);
        nextTickSlot = -1;
    }

    private int searchForTotems(PlayerInventory inventory) {
        totemsLeft = 0;
        int nextTotemSlot = -1;

        for (int i = 0; i <= 36; i++) {
            if (inventory.getStack(i).getItem() == Items.TOTEM_OF_UNDYING) {
                totemsLeft++;
                nextTotemSlot = i < 9 ? i + 36 : i;
            }
        }

        return nextTotemSlot;
    }
}
