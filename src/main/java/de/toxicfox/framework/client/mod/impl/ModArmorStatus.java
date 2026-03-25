package de.toxicfox.framework.client.mod.impl;

import de.toxicfox.framework.client.hud.ScreenPosition;
import de.toxicfox.framework.client.mod.ModDraggable;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ModArmorStatus extends ModDraggable {

    private final int[] slots = new int[]{
            EquipmentSlot.HEAD.getIndex(Inventory.INVENTORY_SIZE),
            EquipmentSlot.CHEST.getIndex(Inventory.INVENTORY_SIZE),
            EquipmentSlot.LEGS.getIndex(Inventory.INVENTORY_SIZE),
            EquipmentSlot.FEET.getIndex(Inventory.INVENTORY_SIZE)
    };

    public ModArmorStatus() {
        pos = new ScreenPosition(0.84, 0);
    }

    @Override
    public int getWidth() {
        return 64;
    }

    @Override
    public int getHeight() {
        return 64;
    }

    @Override
    public void render(GuiGraphicsExtractor drawContext, ScreenPosition pos) {
        for (int i = 0; i < 4; i++) {
            assert mc.player != null;
            ItemStack itemStack = mc.player.getInventory().getItem(slots[i]);
            renderItemStack(drawContext, pos, i, itemStack);
        }
    }

    @Override
    public void renderDummy(GuiGraphicsExtractor drawContext, ScreenPosition pos) {
        renderItemStack(drawContext, pos, 0, new ItemStack(Items.DIAMOND_HELMET));
        renderItemStack(drawContext, pos, 1, new ItemStack(Items.DIAMOND_CHESTPLATE));
        renderItemStack(drawContext, pos, 2, new ItemStack(Items.DIAMOND_LEGGINGS));
        renderItemStack(drawContext, pos, 3, new ItemStack(Items.DIAMOND_BOOTS));
    }

    private void renderItemStack(GuiGraphicsExtractor drawContext, ScreenPosition pos, int i, ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }

        int yAdd = 16 * i;

        if (itemStack.isDamageableItem()) {
            double damage = ((itemStack.getMaxDamage() - itemStack.getDamageValue()) / (double) itemStack.getMaxDamage()) * 100;
            drawContext.text(textRenderer, String.format("%.2f%%", damage), pos.getAbsoluteX() + 20, pos.getAbsoluteY() + yAdd + 5, -1, true);
        }

        drawContext.item(itemStack, pos.getAbsoluteX(), pos.getAbsoluteY() + yAdd);
    }

    @Override
    public String getId() {
        return "armor-display";
    }

    @Override
    public String getName() {
        return "Armor status";
    }

    @Override
    public boolean isHacked() {
        return false;
    }

}
