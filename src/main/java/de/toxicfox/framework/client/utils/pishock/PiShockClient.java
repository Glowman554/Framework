package de.toxicfox.framework.client.utils.pishock;

import de.toxicfox.framework.client.FrameworkClient;
import de.toxicfox.framework.client.utils.pishock.data.GetUserDevices;
import de.toxicfox.framework.client.utils.pishock.data.GetUserIfApiKeyValid;
import de.toxicfox.framework.client.utils.pishock.data.PublishCommand;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.concurrent.CompletionStage;

public class PiShockClient implements WebSocket.Listener, AutoCloseable {
    private final HttpClient client;
    private final WebSocket webSocket;

    private final PiShockApi api;
    private final ArrayList<ShockerCommand> shockers = new ArrayList<>();

    public PiShockClient(String username, String apiKey) {
        client = HttpClient.newHttpClient();

        webSocket = client.newWebSocketBuilder()
                .buildAsync(URI.create(String.format("wss://broker.pishock.com/v2?Username=%s&ApiKey=%s", username, apiKey)), this)
                .join();

        api = new PiShockApi(username, apiKey);
        try {
            GetUserIfApiKeyValid getUserIfApiKeyValid = api.getUserIfAPIKeyValid();
            GetUserDevices getUserDevices = api.getUserDevices(getUserIfApiKeyValid.UserId);


            for (GetUserDevices.UserDevice device : getUserDevices.devices) {
                for (GetUserDevices.Shocker shocker : device.shockers) {
                    shockers.add(new ShockerCommand(device.clientId, shocker.shockerId, getUserIfApiKeyValid.UserId));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void trigger(int intensity, int duration, String origin) {
        JsonNode commands = JsonNode.array();
        for (ShockerCommand shocker : shockers) {
            commands.add(shocker.create(intensity, duration, origin).toJSON());
        }

        JsonNode root = JsonNode.object();
        root.set("Operation", "PUBLISH");
        root.set("PublishCommands", commands);

        String serialized = Json.json().serialize(root);
        webSocket.sendText(serialized, false);
    }

    @Override
    public void onOpen(WebSocket webSocket) {
        FrameworkClient.LOGGER.info("[PiShock] Connected");
        webSocket.request(1);
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        FrameworkClient.LOGGER.info("[PiShock] Received: {}", data);
        webSocket.request(1);
        return null;
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        FrameworkClient.LOGGER.info("[PiShock] Disconnected");
        return null;
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        error.printStackTrace();
    }

    @Override
    public void close() throws Exception {
        webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Bye").thenRun(() -> FrameworkClient.LOGGER.info("[PiShock] Disconnecting..."));
    }

    private static class ShockerCommand {
        private final String channel;
        private final int shockerId;
        private final int userId;

        private ShockerCommand(int clientId, int shockerId, int userId) {
            this.channel = String.format("c%d-ops", clientId);
            this.shockerId = shockerId;
            this.userId = userId;

            FrameworkClient.LOGGER.info("Found PiShock shocker channel: {}, shockerId: {}, userId: {}", channel, shockerId, userId);
        }

        public PublishCommand create(int intensity, int duration, String origin) {
            return PublishCommand.create(channel, shockerId, intensity, duration, userId, origin);
        }
    }
}
