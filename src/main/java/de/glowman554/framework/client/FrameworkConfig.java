package de.glowman554.framework.client;

import de.glowman554.config.Savable;
import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;

public class FrameworkConfig extends AutoSavable {
    @Saved
    public String prefix = ".";
    @Saved
    public boolean enableHacks = false;
    @Saved
    public boolean discordRpc = true;
    @Saved
    public boolean sync = false;

    @Saved(remap = Savable.class)
    public TelemetryConfig telemetry = new TelemetryConfig();
    @Saved(remap = Savable.class)
    public DevelopmentConfig development = new DevelopmentConfig();

    public static class TelemetryConfig extends AutoSavable {
        @Saved
        public boolean debug = false;
        @Saved
        public boolean enable = true;
    }

    public static class DevelopmentConfig extends AutoSavable {
        @Saved
        public boolean singleModFile = true;
        @Saved
        public boolean runGenerators = false;
        @Saved
        public String featuredServersBackend = "https://framework.glowman554.de/api/featured/fetch";
        @Saved
        public String telemetryCollectorBackend = "https://framework.glowman554.de/api/telemetry/collect";
        @Saved
        public String versionInfoBackend = "https://framework.glowman554.de/api/version/{version}";
        @Saved
        public String configTest = "https://framework.glowman554.de/api/config/test";
        @Saved
        public String configSync = "https://framework.glowman554.de/api/config/sync";
    }
}
