package de.toxicfox.framework.client.mod.impl;

import de.toxicfox.framework.client.hud.ScreenPosition;
import de.toxicfox.framework.client.mod.ModDraggable;
import net.minecraft.client.gui.GuiGraphics;

public class ModHeartView extends ModDraggable {

    @Override
    public int getWidth() {
        return textRenderer.width("Oxygen: 20/20");
    }

    @Override
    public int getHeight() {
        return textRenderer.lineHeight * 2;
    }

    @Override
    public void render(GuiGraphics drawContext, ScreenPosition pos) {
        int i = 0;
        drawContext.drawString(textRenderer,
                String.format("Health: %d/%d", (int) mc.player.getHealth(), (int) mc.player.getMaxHealth()),
                pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1 + textRenderer.lineHeight * i++, -1, true);
        drawContext.drawString(textRenderer,
                String.format("Oxygen: %d/%d", (int) mc.player.getAirSupply(), (int) mc.player.getMaxAirSupply()),
                pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1 + textRenderer.lineHeight * i++, -1, true);
    }

    @Override
    public void renderDummy(GuiGraphics drawContext, ScreenPosition pos) {
        int i = 0;
        drawContext.drawString(textRenderer, "Health: 20/20", pos.getAbsoluteX() + 1,
                pos.getAbsoluteY() + 1 + textRenderer.lineHeight * i++, -1, true);
        drawContext.drawString(textRenderer, "Oxygen: 20/20", pos.getAbsoluteX() + 1,
                pos.getAbsoluteY() + 1 + textRenderer.lineHeight * i++, -1, true);
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
