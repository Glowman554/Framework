package de.glowman554.framework.client.mod.impl;

import de.glowman554.framework.client.hud.ScreenPosition;
import de.glowman554.framework.client.mod.ModDraggable;
import net.minecraft.client.gui.DrawContext;

public class ModMemoryDisplay extends ModDraggable {
    @Override
    public int getWidth() {
        return textRenderer.getWidth("Memory: 100% 512/512MB");
    }

    @Override
    public int getHeight() {
        return textRenderer.fontHeight;
    }

    @Override
    public void render(DrawContext drawContext, ScreenPosition pos) {
        Runtime runtime = Runtime.getRuntime();
        long i = Runtime.getRuntime().maxMemory();
        long j = Runtime.getRuntime().totalMemory();
        long k = Runtime.getRuntime().freeMemory();
        long l = j - k;

        drawContext.drawText(textRenderer, String.format("Mem: %d%% %d/%dMB", l * 100L / i, bytesToMb(l), bytesToMb(i)), pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, -1, true);
    }

    private long bytesToMb(long bytes) {
        return bytes / 1024 / 1024;
    }

    @Override
    public void renderDummy(DrawContext drawContext, ScreenPosition pos) {
        drawContext.drawText(textRenderer, "Memory: 100% 512/512MB", pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, -1, true);
    }

    @Override
    public String getId() {
        return "memory-display";
    }

    @Override
    public String getName() {
        return "Memory display";
    }

    @Override
    public boolean isHacked() {
        return false;
    }
}
