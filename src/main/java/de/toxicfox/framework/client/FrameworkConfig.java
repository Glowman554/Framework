package de.toxicfox.framework.client;

import de.toxicfox.config.Savable;
import de.toxicfox.config.auto.AutoSavable;
import de.toxicfox.config.auto.Saved;

public class FrameworkConfig extends AutoSavable {
    @Saved
    public String prefix = ".";
    @Saved
    public boolean enableHacks = false;
    // @Saved
    // public boolean sync = false;
    // @Saved
    // public boolean cloud = true;

    @Saved
    public boolean enableLegacyPiShock = false;

    @Saved(remap = Savable.class)
    public TelemetryConfig telemetry = new TelemetryConfig();
    @Saved(remap = Savable.class)
    public DevelopmentConfig development = new DevelopmentConfig();

    public static class TelemetryConfig extends AutoSavable {
        @Saved
        public boolean debug = false;
    }

    public static class DevelopmentConfig extends AutoSavable {
        @Saved
        public boolean singleModFile = true;
        @Saved
        public boolean runGenerators = false;
        @Saved
        public boolean debugAutoSavable = false;
        @Saved(remap = Savable.class)
        public BackendConfig backend = new BackendConfig();
    }

    public static class BackendConfig extends AutoSavable {
        @Saved
        public String featuredServers = "https://framework.toxicfox.de/api/v1/featuredServers";
        @Saved
        public String telemetryCollector = "https://framework.toxicfox.de/api/v1/telemetry";
        @Saved
        public String versionInfo = "https://framework.toxicfox.de/api/v1/version/{version}";
        @Saved
        public String testToken = "https://framework.toxicfox.de/api/v1/testToken";
        @Saved
        public String profileUpload = "https://framework.toxicfox.de/api/v1/config/uploadProfile";
        @Saved
        public String profileDownload = "https://framework.toxicfox.de/api/v1/config/downloadProfile";
        @Saved
        public String chatPublish = "https://framework.toxicfox.de/api/v1/chat/publish";
        @Saved
        public String chatSubscribe = "wss://framework.toxicfox.de/api/v1/chat/subscribe";
    }

}
