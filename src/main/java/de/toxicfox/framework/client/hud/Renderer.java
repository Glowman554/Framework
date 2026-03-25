package de.toxicfox.framework.client.hud;

import net.minecraft.client.gui.GuiGraphics;

public interface Renderer {
    int getWidth();

    int getHeight();

    void render(GuiGraphics drawContext, ScreenPosition pos);

    default void renderDummy(GuiGraphics drawContext, ScreenPosition pos) {
        render(drawContext, pos);
    }

    default boolean isEnabled() {
        return true;
    }

    ScreenPosition getPos();

    void setPos(ScreenPosition pos);
}
