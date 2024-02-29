package de.glowman554.framework.client.mod.impl;


import de.glowman554.config.Savable;
import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;
import de.glowman554.framework.client.FrameworkClient;
import de.glowman554.framework.client.command.Command;
import de.glowman554.framework.client.command.CommandEvent;
import de.glowman554.framework.client.hud.ScreenPosition;
import de.glowman554.framework.client.mod.ModDraggable;
import de.glowman554.framework.client.registry.FrameworkRegistries;
import de.glowman554.framework.client.telemetry.buildin.TelemetryModCollector;
import de.glowman554.framework.client.utils.WebHook;
import net.minecraft.client.gui.DrawContext;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@TelemetryModCollector.ModTelemetryDisabled
public class ModQueueNotifier extends ModDraggable {
    @Saved(remap = Savable.class)
    protected QueueNotifierConfig config = new QueueNotifierConfig();

    private int position = -1;

    @Override
    public int getWidth() {
        return textRenderer.getWidth("Position: 12");
    }

    @Override
    public int getHeight() {
        return textRenderer.fontHeight;
    }

    @Override
    public void render(DrawContext drawContext, ScreenPosition pos) {
        if (position != -1) {
            drawContext.drawText(textRenderer, String.format("Position: %d", position), pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, -1, true);
        }
    }

    @Override
    public void renderDummy(DrawContext drawContext, ScreenPosition pos) {
        drawContext.drawText(textRenderer, "Position: 12", pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, -1, true);
    }

    public void onChat(String content) {
        if (!isEnabled()) {
            return;
        }
        content = content.replaceAll("§.", "").trim();
        int newPosition = -1;
        for (String regex : config.regexes) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(content);
            while (matcher.find()) {
                newPosition = Integer.parseInt(matcher.group(1));
            }
        }

        if (newPosition != -1 && newPosition != position) {
            position = newPosition;
            if (position <= config.notifyAt) {
                sendNotification(String.format("There are only %d people in the queue! Get ready to play!", position));
            }
            FrameworkClient.LOGGER.info("Got new queue position {}", position);
        }
    }

    private void sendNotification(String content) {
        if (!config.webhook.isEmpty()) {
            WebHook webHook = new WebHook(config.webhook);
            webHook.setUsername("Queue Notification");
            webHook.setContent(content);
            try {
                webHook.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setEnabled(boolean newEnabled) {
        super.setEnabled(newEnabled);

        position = -1;
        if (isEnabled()) {
            FrameworkClient.getInstance().getCommandManager().addCommand("queue-notifier", new QueueNotifierCommand());
        } else {
            FrameworkClient.getInstance().getCommandManager().removeCommand("queue-notifier");
        }
    }

    @Override
    public String getId() {
        return "queue-notifier";
    }

    @Override
    public String getName() {
        return "Queue notifier";
    }

    @Override
    public boolean isHacked() {
        return false;
    }

    public static class QueueNotifierConfig extends AutoSavable {
        @Saved
        protected String webhook;
        @Saved
        protected String[] regexes;
        @Saved
        protected int notifyAt;

        public QueueNotifierConfig() {
            regexes = new String[]{"Position in queue: (\\d*)"};
            notifyAt = 20;
            webhook = "";
        }
    }

    private static class QueueNotifierCommand extends Command {
        public QueueNotifierCommand() {
            super("Set discord webhook!");
        }

        @Override
        public void execute(CommandEvent event) {
            if (event.args().length != 1) {
                event.commandFail("No webhook provided!");
            } else {
                ModQueueNotifier mod = (ModQueueNotifier) FrameworkRegistries.MODS.get(ModQueueNotifier.class);
                mod.config.webhook = event.args()[0];
                mod.save();
            }
        }
    }
}