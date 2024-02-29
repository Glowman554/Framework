package de.glowman554.framework.client.event.impl;


import de.glowman554.framework.client.event.Event;

public class DeathEvent extends Event {
    private final String reason;
    private final String scoreText;

    public DeathEvent(String reason, String scoreText) {
        this.reason = reason;
        this.scoreText = scoreText;
    }

    public String getReason() {
        return reason;
    }

    public String getScoreText() {
        return scoreText;
    }
}
