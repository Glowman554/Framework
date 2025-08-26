package de.toxicfox.framework.client.event.impl;

import de.toxicfox.framework.client.event.Event;
import net.minecraft.client.QuickPlayLogger;

public class WorldJoinEvent extends Event {
    private final QuickPlayLogger.WorldType worldType;
    private final String name;
    private final String id;

    public WorldJoinEvent(QuickPlayLogger.WorldType worldType, String name, String id) {
        this.worldType = worldType;
        this.name = name;
        this.id = id;
    }

    public QuickPlayLogger.WorldType getWorldType() {
        return worldType;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
