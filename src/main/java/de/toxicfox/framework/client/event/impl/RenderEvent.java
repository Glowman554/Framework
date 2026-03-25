package de.toxicfox.framework.client.event.impl;

import de.toxicfox.framework.client.event.Event;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class RenderEvent extends Event {
    private final GuiGraphicsExtractor drawContext;

    public RenderEvent(GuiGraphicsExtractor drawContext) {
        this.drawContext = drawContext;
    }

    public GuiGraphicsExtractor getDrawContext() {
        return drawContext;
    }
}