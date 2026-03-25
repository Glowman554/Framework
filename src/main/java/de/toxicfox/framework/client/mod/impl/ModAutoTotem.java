package de.toxicfox.framework.client.mod.impl;

import de.toxicfox.framework.client.event.EventTarget;
import de.toxicfox.framework.client.event.impl.ClientPlayerTickEvent;
import de.toxicfox.framework.client.hud.ScreenPosition;
import de.toxicfox.framework.client.mod.ModDraggable;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ModAutoTotem extends ModDraggable {
    private int totemsLeft;
    private int nextTickSlot;

    @Override
    public int getWidth() {
        return textRenderer.width("11 totems left");
    }

    @Override
    public int getHeight() {
        return textRenderer.lineHeight;
    }

    @Override
    public void render(GuiGraphicsExtractor drawContext, ScreenPosition pos) {
        drawContext.text(textRenderer, totemsLeft + " totems left", pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, -1, true);
    }

    @Override
    public void renderDummy(GuiGraphicsExtractor drawContext, ScreenPosition pos) {
        drawContext.text(textRenderer, "11 totems left", pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, -1, true);
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

        Inventory inventory = mc.player.getInventory();

        int nextTotemSlot = searchForTotems(mc.player.getInventory());
        ItemStack offhandStack = inventory.getItem(40);
        if (offhandStack.getItem() == Items.TOTEM_OF_UNDYING) {
            totemsLeft++;
            return;
        }


        if ((mc.screen instanceof AbstractContainerScreen<?> && !(mc.screen instanceof InventoryScreen)) || nextTotemSlot == -1) {
            return;
        }

        moveTotem(nextTotemSlot, offhandStack);
    }

    private void moveTotem(int nextTotemSlot, ItemStack offhandStack) {
        boolean offhandEmpty = offhandStack.isEmpty();

        assert mc.gameMode != null;
        mc.gameMode.handleContainerInput(mc.player.inventoryMenu.containerId, nextTotemSlot, 0, ContainerInput.PICKUP, mc.player);
        mc.gameMode.handleContainerInput(mc.player.inventoryMenu.containerId, 45, 0, ContainerInput.PICKUP, mc.player);


        if (!offhandEmpty) {
            nextTickSlot = nextTotemSlot;
        }
    }

    private void finishMovingTotem() {
        if (nextTickSlot == -1) {
            return;
        }

        assert mc.gameMode != null;
        mc.gameMode.handleContainerInput(mc.player.inventoryMenu.containerId, nextTickSlot, 0, ContainerInput.PICKUP, mc.player);
        nextTickSlot = -1;
    }

    private int searchForTotems(Inventory inventory) {
        totemsLeft = 0;
        int nextTotemSlot = -1;

        for (int i = 0; i <= 36; i++) {
            if (inventory.getItem(i).getItem() == Items.TOTEM_OF_UNDYING) {
                totemsLeft++;
                nextTotemSlot = i < 9 ? i + 36 : i;
            }
        }

        return nextTotemSlot;
    }

    @Override
    public boolean isHacked() {
        return true;
    }
}
