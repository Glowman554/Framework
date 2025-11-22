package de.toxicfox.framework.client.event.impl;

import de.toxicfox.framework.client.event.Event;
import net.minecraft.client.gui.hud.ChatHudLine;

public class ChatEvent extends Event {
    private final ChatHudLine message;

    public ChatEvent(ChatHudLine message) {
        this.message = message;
    }

    public ChatHudLine getMessage() {
        return message;
    }

    public String cleanContent() {
        return message.content().getString().replaceAll("§.", "").trim();
    }
}
