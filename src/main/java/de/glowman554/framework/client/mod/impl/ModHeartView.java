package de.glowman554.framework.client.mod.impl;

import de.glowman554.framework.client.hud.ScreenPosition;
import de.glowman554.framework.client.mod.ModDraggable;
import net.minecraft.client.gui.DrawContext;

public class ModHeartView extends ModDraggable {

    @Override
    public int getWidth() {
        return textRenderer.getWidth("Oxygen: 20/20");
    }

    @Override
    public int getHeight() {
        return textRenderer.fontHeight * 2;
    }

    @Override
    public void render(DrawContext drawContext, ScreenPosition pos) {
        int i = 0;
        drawContext.drawText(textRenderer,
                String.format("Health: %d/%d", (int) mc.player.getHealth(), (int) mc.player.getMaxHealth()),
                pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1 + textRenderer.fontHeight * i++, -1, true);
        drawContext.drawText(textRenderer,
                String.format("Oxygen: %d/%d", (int) mc.player.getAir(), (int) mc.player.getMaxAir()),
                pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1 + textRenderer.fontHeight * i++, -1, true);
    }

    @Override
    public void renderDummy(DrawContext drawContext, ScreenPosition pos) {
        int i = 0;
        drawContext.drawText(textRenderer, "Health: 20/20", pos.getAbsoluteX() + 1,
                pos.getAbsoluteY() + 1 + textRenderer.fontHeight * i++, -1, true);
        drawContext.drawText(textRenderer, "Oxygen: 20/20", pos.getAbsoluteX() + 1,
                pos.getAbsoluteY() + 1 + textRenderer.fontHeight * i++, -1, true);
    }

    @Override
    public String getId() {
        return "heart-view";
    }

    @Override
    public String getName() {
        return "Heart view";
    }

    @Override
    public boolean isHacked() {
        return false;
    }

}
