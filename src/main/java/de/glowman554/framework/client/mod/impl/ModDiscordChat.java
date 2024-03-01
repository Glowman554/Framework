package de.glowman554.framework.client.mod.impl;

import de.glowman554.config.auto.Saved;
import de.glowman554.framework.client.FrameworkClient;
import de.glowman554.framework.client.command.Command;
import de.glowman554.framework.client.command.CommandEvent;
import de.glowman554.framework.client.config.Configurable;
import de.glowman554.framework.client.event.EventTarget;
import de.glowman554.framework.client.event.impl.ChatEvent;
import de.glowman554.framework.client.mod.Mod;
import de.glowman554.framework.client.registry.FrameworkRegistries;
import de.glowman554.framework.client.telemetry.buildin.TelemetryModCollector;
import de.glowman554.framework.client.utils.WebHook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

@TelemetryModCollector.ModTelemetryDisabled
public class ModDiscordChat extends Mod {
    private final ArrayList<ChatEvent> senderQueue = new ArrayList<>();
    @Saved
    @Configurable(text = "Discord webhook")
    protected String webhook = "";
    private Timer timer;

    @Override
    public String getId() {
        return "discord-chat";
    }

    @Override
    public String getName() {
        return "Discord chat";
    }

    @Override
    public void setEnabled(boolean newEnabled) {
        super.setEnabled(newEnabled);

        if (isEnabled()) {
            FrameworkClient.getInstance().getCommandManager().addCommand("discord-chat", new DiscordChatCommand());
            if (timer == null) {
                timer = new Timer();
                timer.scheduleAtFixedRate(getTimerTask(), 0, 100);
            }
        } else {
            FrameworkClient.getInstance().getCommandManager().removeCommand("discord-chat");
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }
    }

    private TimerTask getTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                if (webhook.isEmpty()) {
                    return;
                }
                ChatEvent message = null;
                synchronized (senderQueue) {
                    if (!senderQueue.isEmpty()) {
                        message = senderQueue.get(0);
                        senderQueue.remove(0);
                    }
                }

                if (message != null) {
                    WebHook webHook = new WebHook(webhook);
                    webHook.setUsername(message.getPlayer().getName());
                    webHook.setAvatarUrl(String.format("https://minotar.net/avatar/%s/128.png", message.getPlayer().getId().toString()));
                    webHook.setContent("`" + message.getMessage() + "`");
                    try {
                        webHook.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    FrameworkClient.LOGGER.info("New send queue size {}", senderQueue.size());
                }
            }
        };
    }

    @EventTarget
    public void onChat(ChatEvent event) {
        synchronized (senderQueue) {
            senderQueue.add(event);
        }
    }

    @Override
    public boolean isHacked() {
        return false;
    }

    private static class DiscordChatCommand extends Command {
        public DiscordChatCommand() {
            super("Set discord webhook!");
        }

        @Override
        public void execute(CommandEvent event) {
            if (event.args().length != 1) {
                event.commandFail("No webhook provided!");
            } else {
                ModDiscordChat mod = (ModDiscordChat) FrameworkRegistries.MODS.get(ModDiscordChat.class);
                mod.webhook = event.args()[0];
                mod.save();
            }
        }
    }
}