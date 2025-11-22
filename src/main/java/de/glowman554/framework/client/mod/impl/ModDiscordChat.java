package de.glowman554.framework.client.mod.impl;

import de.glowman554.config.auto.Saved;
import de.glowman554.framework.client.FrameworkClient;
import de.glowman554.framework.client.config.Configurable;
import de.glowman554.framework.client.event.EventTarget;
import de.glowman554.framework.client.event.impl.ChatEvent;
import de.glowman554.framework.client.mod.Mod;
import de.glowman554.framework.client.utils.WebHook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ModDiscordChat extends Mod {
    private final ArrayList<String> senderQueue = new ArrayList<>();
    private Timer timer;

    @Configurable(text = "Webhook URL")
    @Saved
    private String webhookUrl = "";

    @Override
    public void setEnabled(boolean newEnabled) {
        super.setEnabled(newEnabled);

        if (isEnabled()) {
            if (timer == null) {
                timer = new Timer();
                timer.scheduleAtFixedRate(getTimerTask(), 0, 100);
            }
        } else {
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
                if (webhookUrl.isEmpty()) {
                    return;
                }

                String message = null;
                synchronized (senderQueue) {
                    if (!senderQueue.isEmpty()) {
                        message = senderQueue.get(0);
                        senderQueue.remove(0);
                    }
                }

                if (message != null) {
                    WebHook webHook = new WebHook(webhookUrl);
                    webHook.setUsername("Minecraft Chat");
                    webHook.setContent("`" + message + "`");
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
            senderQueue.add(event.cleanContent());
        }
    }


    @Override
    public String getId() {
        return "discord-chat";
    }

    @Override
    public String getName() {
        return "Discord chat";
    }

    @Override
    public boolean isHacked() {
        return false;
    }
}
