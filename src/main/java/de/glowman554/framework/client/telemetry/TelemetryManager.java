package de.glowman554.framework.client.telemetry;

import de.glowman554.framework.client.FrameworkClient;
import de.glowman554.framework.client.registry.FrameworkRegistries;
import net.minecraft.client.MinecraftClient;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TelemetryManager extends TimerTask {
    private final String prefix = "[Telemetry] ";
    private final int sessionId;
    private final ArrayList<URL> endpoints = new ArrayList<>();
    private boolean debug = false;

    public TelemetryManager() {
        sessionId = new Random().nextInt();
        FrameworkClient.LOGGER.info(prefix + "sessionId: " + sessionId);

        boolean disabled = MinecraftClient.getInstance().getGameProfile().getName().matches("Player\\d+");
        if (FrameworkClient.getInstance().getConfig().telemetry.enable && !disabled) {
            Timer timer = new Timer("Telemetry timer");
            timer.scheduleAtFixedRate(this, 0, 1000);
        } else {
            FrameworkClient.LOGGER.info(prefix + "Disabling telemetry");

        }
    }

    @NotNull
    private HttpURLConnection getHttpURLConnection(String serialized, URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setDoOutput(true);
        con.setRequestMethod("POST");
        // con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64)
        // AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Content-Length", String.valueOf(serialized.length()));
        con.setRequestProperty("Content-Type", "application/json");
        return con;
    }

    @Override
    public void run() {
        try {

            ArrayList<TelemetryCollector> readyCollectors = new ArrayList<>();
            for (TelemetryCollector collector : FrameworkRegistries.TELEMETRY_COLLECTORS.getRegistry().values()) {
                if (collector.collect()) {
                    readyCollectors.add(collector);
                }
            }

            if (!readyCollectors.isEmpty()) {
                JsonNode packet = JsonNode.object();
                packet.set("id", getIdentifier().toJSON());

                for (TelemetryCollector collector : readyCollectors) {
                    packet.set(collector.id(), collector.json());
                }

                sendJson(packet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendJson(JsonNode node) throws IOException {
        String serialized = Json.json().serialize(node);
        if (debug) {
            FrameworkClient.LOGGER.info(prefix + "SENDING: {}", serialized);
        }

        for (URL url : endpoints) {
            HttpURLConnection con = getHttpURLConnection(serialized, url);
            con.getOutputStream().write(serialized.getBytes());

            StringBuilder response = new StringBuilder();

            for (byte b : con.getInputStream().readAllBytes()) {
                response.append((char) b);
            }

            con.getInputStream().close();
            con.disconnect();

            if (debug) {
                FrameworkClient.LOGGER.info(prefix + "RESPONSE[{}]: {}", url, response);
            }
        }
    }

    public void addEndpoint(URL url) {
        FrameworkClient.LOGGER.info(prefix + "ENDPOINT: {}", url.toString());
        endpoints.add(url);
    }

    public TelemetryIdentifier getIdentifier() {
        return new TelemetryIdentifier(MinecraftClient.getInstance().getGameProfile().getName(), sessionId);
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}