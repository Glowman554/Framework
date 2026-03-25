package de.toxicfox.framework.client.event.impl;

import de.toxicfox.framework.client.event.Event;
import net.minecraft.client.gui.GuiGraphics;

public class RenderEvent extends Event {
    private final GuiGraphics drawContext;

    public RenderEvent(GuiGraphics drawContext) {
        this.drawContext = drawContext;
    }

    public GuiGraphics getDrawContext() {
        return drawContext;
    }
}