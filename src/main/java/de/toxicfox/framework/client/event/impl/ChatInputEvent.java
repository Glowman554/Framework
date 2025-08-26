package de.toxicfox.framework.client.event.impl;

import de.toxicfox.framework.client.event.EventCancelable;

public class ChatInputEvent extends EventCancelable {
    private final String content;

    public ChatInputEvent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
