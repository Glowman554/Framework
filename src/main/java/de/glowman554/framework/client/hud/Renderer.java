package de.glowman554.framework.client.hud;

import net.minecraft.client.gui.DrawContext;

public interface Renderer {
    int getWidth();

    int getHeight();

    void render(DrawContext drawContext, ScreenPosition pos);

    default void renderDummy(DrawContext drawContext, ScreenPosition pos) {
        render(drawContext, pos);
    }

    default boolean isEnabled() {
        return true;
    }

    ScreenPosition getPos();

    void setPos(ScreenPosition pos);
}
