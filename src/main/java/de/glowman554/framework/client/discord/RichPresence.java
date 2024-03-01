package de.glowman554.framework.client.discord;

import de.glowman554.framework.client.FrameworkClient;
import de.glowman554.framework.client.event.EventManager;
import de.glowman554.framework.client.event.EventTarget;
import de.glowman554.framework.client.event.impl.WindowOpeningEvent;
import de.glowman554.framework.client.event.impl.WorldJoinEvent;
import de.glowman554.framework.client.registry.FrameworkRegistries;
import de.glowman554.framework.client.telemetry.buildin.TelemetryDiscordUserCollector;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.DiscordUser;

public class RichPresence {
    private boolean running = true;
    private long created = 0;

    private DiscordUser discordUser = null;

    public void start() {
        if (FrameworkClient.getInstance().getConfig().discordRpc) {
            EventManager.register(this);
            this.created = System.currentTimeMillis();

            DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(discordUser -> {
                this.discordUser = discordUser;
                FrameworkClient.LOGGER.info("Welcome " + discordUser.username + "#" + discordUser.discriminator);

                try {
                    TelemetryDiscordUserCollector collector = (TelemetryDiscordUserCollector) FrameworkRegistries.TELEMETRY_COLLECTORS.get(TelemetryDiscordUserCollector.class);
                    collector.setUser(discordUser);
                } catch (IllegalArgumentException ignored) {

                }

                update("Starting...", "");
            }).build();

            String client_id = "1213159731968081980";
            DiscordRPC.discordInitialize(client_id, handlers, true);

            new Thread("Discord RP Callback") {
                @Override
                public void run() {
                    while (running) {
                        DiscordRPC.discordRunCallbacks();
                    }
                }
            }.start();
        }
    }

    @EventTarget
    public void onWorldJoin(WorldJoinEvent event) {
        if (event.getWorldType() == null) {
            update("Idle", "");
        } else {
            switch (event.getWorldType()) {
                case SINGLEPLAYER:
                    update("Playing singleplayer", event.getName());
                    break;
                case MULTIPLAYER:
                    update("Playing multiplayer", event.getId());
                    break;
                case REALMS:
                    update("Playing realms", "");
                    break;
            }
        }
    }

    @EventTarget
    public void onWindowOpen(WindowOpeningEvent event) {
        update("Idle", "");
    }

    public void shutdown() {
        running = false;
        DiscordRPC.discordShutdown();
    }

    public void update(String firstLine, String secondLine) {
        if (FrameworkClient.getInstance().getConfig().discordRpc) {
            DiscordRichPresence.Builder rich = new DiscordRichPresence.Builder(secondLine);
            rich.setBigImage("large", "");
            rich.setDetails(firstLine);
            rich.setStartTimestamps(created);
            DiscordRPC.discordUpdatePresence(rich.build());
        }
    }
}
