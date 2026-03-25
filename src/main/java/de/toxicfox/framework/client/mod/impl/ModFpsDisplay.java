package de.toxicfox.framework.client.mod.impl;

import de.toxicfox.framework.client.hud.ScreenPosition;
import de.toxicfox.framework.client.mod.ModDraggable;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class ModFpsDisplay extends ModDraggable {
    public ModFpsDisplay() {
        pos = new ScreenPosition(0, 0);
    }

    @Override
    public int getWidth() {
        return textRenderer.width("FPS: 120");
    }

    @Override
    public int getHeight() {
        return textRenderer.lineHeight;
    }

    @Override
    public void render(GuiGraphicsExtractor drawContext, ScreenPosition pos) {
        drawContext.text(textRenderer, "FPS: " + mc.getFps(), pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, -1, true);
    }

    @Override
    public void renderDummy(GuiGraphicsExtractor drawContext, ScreenPosition pos) {
        drawContext.text(textRenderer, "FPS: 120", pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, -1, true);
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
