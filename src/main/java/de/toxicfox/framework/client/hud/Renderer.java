package de.toxicfox.framework.client.hud;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public interface Renderer {
    int getWidth();

    int getHeight();

    void render(GuiGraphicsExtractor drawContext, ScreenPosition pos);

    default void renderDummy(GuiGraphicsExtractor drawContext, ScreenPosition pos) {
        render(drawContext, pos);
    }

    default boolean isEnabled() {
        return true;
    }

    ScreenPosition getPos();

    void setPos(ScreenPosition pos);
}
