package de.glowman554.framework.client.mod.impl;

import de.glowman554.framework.client.hud.ScreenPosition;
import de.glowman554.framework.client.mod.ModDraggable;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ModArmorStatus extends ModDraggable {
    @Override
    public int getWidth() {
        return 64;
    }

    @Override
    public int getHeight() {
        return 64;
    }

    @Override
    public void render(DrawContext drawContext, ScreenPosition pos) {
        for (int i = 0; i < mc.player.getInventory().armor.toArray().length; i++) {
            ItemStack itemStack = mc.player.getInventory().armor.get(i);
            renderItemStack(drawContext, pos, i, itemStack);
        }
    }

    @Override
    public void renderDummy(DrawContext drawContext, ScreenPosition pos) {
        renderItemStack(drawContext, pos, 3, new ItemStack(Items.DIAMOND_HELMET));
        renderItemStack(drawContext, pos, 2, new ItemStack(Items.DIAMOND_CHESTPLATE));
        renderItemStack(drawContext, pos, 1, new ItemStack(Items.DIAMOND_LEGGINGS));
        renderItemStack(drawContext, pos, 0, new ItemStack(Items.DIAMOND_BOOTS));
    }

    private void renderItemStack(DrawContext drawContext, ScreenPosition pos, int i, ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }

        int yAdd = (-16 * i) + 48;

        if (itemStack.isDamageable()) {
            double damage = ((itemStack.getMaxDamage() - itemStack.getDamage()) / (double) itemStack.getMaxDamage()) * 100;
            drawContext.drawText(textRenderer, String.format("%.2f%%", damage), pos.getAbsoluteX() + 20, pos.getAbsoluteY() + yAdd + 5, -1, true);
        }

        drawContext.drawItem(itemStack, pos.getAbsoluteX(), pos.getAbsoluteY() + yAdd);
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
