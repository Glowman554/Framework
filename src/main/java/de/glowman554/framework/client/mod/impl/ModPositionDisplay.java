package de.glowman554.framework.client.mod.impl;

import de.glowman554.framework.client.hud.ScreenPosition;
import de.glowman554.framework.client.mod.ModDraggable;
import net.minecraft.client.gui.DrawContext;

public class ModPositionDisplay extends ModDraggable {
    @Override
    public int getWidth() {
        return textRenderer.getWidth("Facing: east");
    }

    @Override
    public int getHeight() {
        return textRenderer.fontHeight * 2;
    }

    @Override
    public void render(DrawContext drawContext, ScreenPosition pos) {
        assert mc.player != null;
        drawContext.drawText(textRenderer, String.format("XYZ: %d, %d, %d", (int) mc.player.getPos().x, (int) mc.player.getPos().y, (int) mc.player.getPos().z), pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, -1, true);
        drawContext.drawText(textRenderer, "Facing: " + mc.player.getHorizontalFacing(), pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1 + textRenderer.fontHeight, -1, true);
    }

    @Override
    public void renderDummy(DrawContext drawContext, ScreenPosition pos) {
        drawContext.drawText(textRenderer, "XYZ: 0, 0, 0", pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, -1, true);
        drawContext.drawText(textRenderer, "Facing: east", pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1 + textRenderer.fontHeight, -1, true);
    }

    @Override
    public String getId() {
        return "position-display";
    }

    @Override
    public String getName() {
        return "Position";
    }

    @Override
    public boolean isHacked() {
        return false;
    }
}
