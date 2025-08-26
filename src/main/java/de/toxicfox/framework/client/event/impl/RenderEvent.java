package de.toxicfox.framework.client.event.impl;

import de.toxicfox.framework.client.event.Event;
import net.minecraft.client.gui.DrawContext;

public class RenderEvent extends Event {
    private final DrawContext drawContext;

    public RenderEvent(DrawContext drawContext) {
        this.drawContext = drawContext;
    }

    public DrawContext getDrawContext() {
        return drawContext;
    }
}