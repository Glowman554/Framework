package de.toxicfox.framework.client.mod.impl;

import de.toxicfox.framework.client.hud.ScreenPosition;
import de.toxicfox.framework.client.mod.ModDraggable;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import java.util.Objects;

public class ModPingDisplay extends ModDraggable {
    public ModPingDisplay() {
        pos = new ScreenPosition(0, 0.03);
    }

    @Override
    public int getWidth() {
        return textRenderer.width("0 ms");
    }

    @Override
    public int getHeight() {
        return textRenderer.lineHeight;
    }

    @Override
    public void render(GuiGraphicsExtractor drawContext, ScreenPosition pos) {
        if (!mc.isLocalServer()) {
            drawContext.text(textRenderer, Objects.requireNonNull(mc.getCurrentServer()).ping + " ms", pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, -1, true);
        } else {
            drawContext.text(textRenderer, "0 ms", pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, -1, true);
        }
    }

    @Override
    public void renderDummy(GuiGraphicsExtractor drawContext, ScreenPosition pos) {
        drawContext.text(textRenderer, "0 ms", pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, -1, true);
    }

    @Override
    public String getId() {
        return "ping-display";
    }

    @Override
    public String getName() {
        return "Ping display";
    }

    @Override
    public boolean isHacked() {
        return false;
    }
}
