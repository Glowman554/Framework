package de.glowman554.framework.client.event.impl;

import com.mojang.authlib.GameProfile;
import de.glowman554.framework.client.event.Event;

public class ChatEvent extends Event {
    private final String message;
    private final GameProfile player;

    public ChatEvent(String message, GameProfile player) {
        this.message = message.replaceAll("§.", "").trim();
        this.player = player;
    }

    public String getMessage() {
        return message;
    }

    public GameProfile getPlayer() {
        return player;
    }
}
