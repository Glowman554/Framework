package de.toxicfox.framework.client.event.impl;

import de.toxicfox.framework.client.event.Event;
import net.minecraft.client.quickplay.QuickPlayLog;

public class WorldJoinEvent extends Event {
    private final QuickPlayLog.Type worldType;
    private final String name;
    private final String id;

    public WorldJoinEvent(QuickPlayLog.Type worldType, String name, String id) {
        this.worldType = worldType;
        this.name = name;
        this.id = id;
    }

    public QuickPlayLog.Type getWorldType() {
        return worldType;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
