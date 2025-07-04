package de.glowman554.framework.client.event.impl;

import de.glowman554.framework.client.event.EventCancelable;

public class ChatInputEvent extends EventCancelable {
    private final String content;

    public ChatInputEvent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
