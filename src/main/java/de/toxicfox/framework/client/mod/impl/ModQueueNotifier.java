package de.toxicfox.framework.client.mod.impl;


import de.toxicfox.config.auto.Saved;
import de.toxicfox.framework.client.FrameworkClient;
import de.toxicfox.framework.client.command.Command;
import de.toxicfox.framework.client.command.CommandEvent;
import de.toxicfox.framework.client.config.Configurable;
import de.toxicfox.framework.client.hud.ScreenPosition;
import de.toxicfox.framework.client.mod.ModDraggable;
import de.toxicfox.framework.client.registry.FrameworkRegistries;
import de.toxicfox.framework.client.telemetry.buildin.TelemetryModCollector;
import de.toxicfox.framework.client.utils.WebHook;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.gui.GuiGraphics;

@TelemetryModCollector.Disabled
public class ModQueueNotifier extends ModDraggable {
    @Saved
    @Configurable(text = "Discord webhook")
    protected String webhook = "";
    @Saved
    @Configurable(text = "Position regexes")
    protected String[] regexes = new String[]{"Position in queue: (\\d*)"};
    @Saved
    @Configurable(text = "Notification position")
    protected int notifyAt = 20;


    private int position = -1;

    @Override
    public int getWidth() {
        return textRenderer.width("Position: 12");
    }

    @Override
    public int getHeight() {
        return textRenderer.lineHeight;
    }

    @Override
    public void render(GuiGraphics drawContext, ScreenPosition pos) {
        if (position != -1) {
            drawContext.drawString(textRenderer, String.format("Position: %d", position), pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, -1, true);
        }
    }

    @Override
    public void renderDummy(GuiGraphics drawContext, ScreenPosition pos) {
        drawContext.drawString(textRenderer, "Position: 12", pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, -1, true);
    }

    public void onChat(String content) {
        if (!isEnabled()) {
            return;
        }
        content = content.replaceAll("§.", "").trim();
        int newPosition = -1;
        for (String regex : regexes) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(content);
            while (matcher.find()) {
                newPosition = Integer.parseInt(matcher.group(1));
            }
        }

        if (newPosition != -1 && newPosition != position) {
            position = newPosition;
            if (position <= notifyAt) {
                sendNotification(String.format("There are only %d people in the queue! Get ready to play!", position));
            }
            FrameworkClient.LOGGER.info("Got new queue position {}", position);
        }
    }

    private void sendNotification(String content) {
        if (!webhook.isEmpty()) {
            WebHook webHook = new WebHook(webhook);
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
                mod.webhook = event.args()[0];
                mod.save();
            }
        }
    }
}