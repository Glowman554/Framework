package de.toxicfox.framework.client.mod.impl;

import de.toxicfox.config.auto.AutoSavable;
import de.toxicfox.config.auto.Saved;
import de.toxicfox.framework.client.FrameworkClient;
import de.toxicfox.framework.client.config.Configurable;
import de.toxicfox.framework.client.event.EventTarget;
import de.toxicfox.framework.client.event.impl.ChatInputEvent;
import de.toxicfox.framework.client.mod.Mod;
import de.toxicfox.framework.client.utils.Memoizer;
import de.toxicfox.framework.client.utils.WebClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.text.Text;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;
import net.shadew.json.JsonSyntaxException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class ModGlobalChat extends Mod {
    private final Json json = Json.json();
    private String token;
    private Subscription subscription;

    private final ArrayList<String> publishQueue = new ArrayList<>();
    private Timer timer;

    @Saved
    @Configurable(text = "Prefix for global chat messages")
    private String prefix = "@";

    @Override
    public String getId() {
        return "global-chat";
    }

    @Override
    public String getName() {
        return "Global chat";
    }

    @Override
    public boolean isHacked() {
        return false;
    }

    @Override
    public void setEnabled(boolean newEnabled) {
        super.setEnabled(newEnabled);

        if (isEnabled()) {
            token = MinecraftClient.getInstance().getSession().getAccessToken();
            if (subscription == null) {
                subscription = new Subscription();
            }
            if (timer == null) {
                timer = new Timer();
                timer.scheduleAtFixedRate(getTimerTask(), 0, 100);
            }
        } else {
            token = null;
            if (subscription != null) {
                try {
                    subscription.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                subscription = null;
            }
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
                String message = null;
                synchronized (publishQueue) {
                    if (!publishQueue.isEmpty()) {
                        message = publishQueue.getFirst();
                        publishQueue.removeFirst();
                    }
                }

                if (message != null) {
                    if (!ready()) {
                        return;
                    }

                    try {
                        JsonNode root = JsonNode.object();
                        root.set("message", message);

                        WebClient.post(FrameworkClient.getInstance().getConfig().development.backend.chatPublish, json.serialize(root), Map.of("Authentication", token));
                    } catch (IOException e) {
                        FrameworkClient.LOGGER.error("Could not publish chat message", e);
                    }
                }
            }
        };
    }

    private final Function<String, Boolean> readyImpl = Memoizer.memoize((token) -> {
        try {
            String result = WebClient.get(FrameworkClient.getInstance().getConfig().development.backend.testToken, Map.of("Authentication", token));
            JsonNode root = json.parse(result);

            FrameworkClient.LOGGER.info("profile user: {}", root.get("name").asString());
        } catch (IOException e) {
            return false;
        }
        return true;
    });

    private boolean ready() {
        return readyImpl.apply(token);
    }

    private void publish(String message) {
        synchronized (publishQueue) {
            publishQueue.add(message);
        }
    }

    @EventTarget
    public void onChatInput(ChatInputEvent event) {
        if (event.getContent().startsWith(prefix)) {
            String message = event.getContent().substring(prefix.length());
            publish(message);
            event.setCanceled(true);
        }
    }

    private void processMessage(Message message) {
        FrameworkClient.LOGGER.info("[Global chat] {}: {}", message.username, message.message);
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of(String.format("§7[Global] §3<%s> §r%s", message.username, message.message)), null, MessageIndicator.notSecure());
    }

    private class Subscription implements WebSocket.Listener, AutoCloseable {
        private final HttpClient client;
        private final WebSocket webSocket;

        public Subscription() {
            client = HttpClient.newHttpClient();

            webSocket = client.newWebSocketBuilder()
                    .buildAsync(URI.create(FrameworkClient.getInstance().getConfig().development.backend.chatSubscribe), this)
                    .join();
        }

        @Override
        public void onOpen(WebSocket webSocket) {
            FrameworkClient.LOGGER.info("[Global chat] Connected");
            webSocket.request(1);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            webSocket.request(1);

            Message message = new Message();
            try {
                message.fromJSON(json.parse(String.valueOf(data)));

                processMessage(message);
            } catch (JsonSyntaxException e) {
                FrameworkClient.LOGGER.error("Could not parse chat message", e);
                return null;
            }

            return null;
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            FrameworkClient.LOGGER.info("[Global chat] Disconnected");
            return null;
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            error.printStackTrace();
        }

        @Override
        public void close() throws Exception {
            webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Bye").thenRun(() -> {
                FrameworkClient.LOGGER.info("[Global chat] Disconnecting...");
                client.close();
            });

        }
    }

    private static class Message extends AutoSavable {
        @Saved
        public String username;
        @Saved
        public int id;
        @Saved
        public String message;
    }
}
