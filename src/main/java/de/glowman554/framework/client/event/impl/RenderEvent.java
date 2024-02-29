package de.glowman554.framework.client.event.impl;

import de.glowman554.framework.client.event.Event;
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