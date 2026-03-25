package de.toxicfox.framework.client.mod.impl;

import de.toxicfox.framework.client.hud.ScreenPosition;
import de.toxicfox.framework.client.mod.ModDraggable;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class ModPositionDisplay extends ModDraggable {
    public ModPositionDisplay() {
        pos = new ScreenPosition(0.15, 0);
    }

    @Override
    public int getWidth() {
        return textRenderer.width("Facing: east");
    }

    @Override
    public int getHeight() {
        return textRenderer.lineHeight * 2;
    }

    @Override
    public void render(GuiGraphicsExtractor drawContext, ScreenPosition pos) {
        assert mc.player != null;
        drawContext.text(textRenderer, String.format("XYZ: %d, %d, %d", (int) mc.player.position().x, (int) mc.player.position().y, (int) mc.player.position().z), pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, -1, true);
        drawContext.text(textRenderer, "Facing: " + mc.player.getDirection(), pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1 + textRenderer.lineHeight, -1, true);
    }

    @Override
    public void renderDummy(GuiGraphicsExtractor drawContext, ScreenPosition pos) {
        drawContext.text(textRenderer, "XYZ: 0, 0, 0", pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, -1, true);
        drawContext.text(textRenderer, "Facing: east", pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1 + textRenderer.lineHeight, -1, true);
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
