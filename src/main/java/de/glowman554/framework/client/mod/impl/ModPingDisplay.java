package de.glowman554.framework.client.mod.impl;

import de.glowman554.framework.client.hud.ScreenPosition;
import de.glowman554.framework.client.mod.ModDraggable;
import net.minecraft.client.gui.DrawContext;

import java.util.Objects;

public class ModPingDisplay extends ModDraggable {
    @Override
    public int getWidth() {
        return textRenderer.getWidth("0 ms");
    }

    @Override
    public int getHeight() {
        return textRenderer.fontHeight;
    }

    @Override
    public void render(DrawContext drawContext, ScreenPosition pos) {
        if (!mc.isInSingleplayer()) {
            drawContext.drawText(textRenderer, Objects.requireNonNull(mc.getCurrentServerEntry()).ping + " ms", pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, -1, true);
        } else {
            drawContext.drawText(textRenderer, "0 ms", pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, -1, true);
        }
    }

    @Override
    public void renderDummy(DrawContext drawContext, ScreenPosition pos) {
        drawContext.drawText(textRenderer, "0 ms", pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, -1, true);
    }

    @Override
    public String getId() {
        return "ping-display";
    }

    @Override
    public String getName() {
        return "Ping display";
    }
}
