package de.glowman554.framework.client.mod.impl;

import de.glowman554.framework.client.hud.ScreenPosition;
import de.glowman554.framework.client.mod.ModDraggable;
import net.minecraft.client.gui.DrawContext;

public class ModFpsDisplay extends ModDraggable {
    @Override
    public int getWidth() {
        return textRenderer.getWidth("FPS: 120");
    }

    @Override
    public int getHeight() {
        return textRenderer.fontHeight;
    }

    @Override
    public void render(DrawContext drawContext, ScreenPosition pos) {
        drawContext.drawText(textRenderer, "FPS: " + mc.getCurrentFps(), pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, -1, true);
    }

    @Override
    public void renderDummy(DrawContext drawContext, ScreenPosition pos) {
        drawContext.drawText(textRenderer, "FPS: 120", pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, -1, true);
    }

    @Override
    public String getId() {
        return "fps";
    }

    @Override
    public String getName() {
        return "FPS";
    }

    @Override
    public boolean isHacked() {
        return false;
    }
}
