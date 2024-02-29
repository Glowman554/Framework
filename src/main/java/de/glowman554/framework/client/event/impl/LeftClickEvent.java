package de.glowman554.framework.client.event.impl;

import de.glowman554.framework.client.event.EventCancelable;

public class LeftClickEvent extends EventCancelable {
    private final Mode mode;

    public LeftClickEvent(Mode mode) {
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }

    public enum Mode {
        ATTACK, BREAK
    }
}
