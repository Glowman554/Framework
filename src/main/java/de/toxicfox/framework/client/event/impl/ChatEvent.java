package de.toxicfox.framework.client.event.impl;

import de.toxicfox.framework.client.event.Event;
import net.minecraft.client.GuiMessage;

public class ChatEvent extends Event {
    private final GuiMessage message;

    public ChatEvent(GuiMessage message) {
        this.message = message;
    }

    public GuiMessage getMessage() {
        return message;
    }

    public String cleanContent() {
        return message.content().getString().replaceAll("§.", "").trim();
    }
}
