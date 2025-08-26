package de.glowman554.framework.client.telemetry;

import de.toxicfox.config.auto.AutoSavable;
import de.toxicfox.config.auto.Saved;

public class TelemetryIdentifier extends AutoSavable {
    @Saved
    private final String playerName;
    @Saved
    private final int sessionId;

    public TelemetryIdentifier(String playerName, int sessionId) {
        this.playerName = playerName;
        this.sessionId = sessionId;
    }
}