package de.glowman554.framework.client;

import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;

public class FrameworkConfig extends AutoSavable {
    @Saved
    public String prefix = ".";
    @Saved
    public String featuredServersBackend = "https://glowman554.de/api/framework/featrued_servers";
    @Saved
    public boolean frameworkTelemetry = true;
    @Saved
    public boolean enableTelemetryDebug = false;
    @Saved
    public boolean runDataGenerators = false;
    @Saved
    public boolean enableHacks = false;
}
