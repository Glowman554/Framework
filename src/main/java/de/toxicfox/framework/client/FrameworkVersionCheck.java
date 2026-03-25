package de.toxicfox.framework.client;

import de.toxicfox.framework.client.utils.WebClient;
import net.minecraft.DetectedVersion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

import java.io.IOException;
import java.util.Map;

public class FrameworkVersionCheck {

    public static void performVersionCheck() {
        String currentVersion = DetectedVersion.tryDetectVersion().name();

        try {
            String result = WebClient.get(FrameworkClient.getInstance().getConfig().development.backend.versionInfo.replace("{version}", currentVersion), Map.of());
            JsonNode root = Json.json().parse(result);

            JsonNode error = root.get("error");
            if (error != null) {
                FrameworkClient.LOGGER.error("Failed to fetch version info: {}", error.asString());
                return;
            }

            if (root.get("endOfLife").asBoolean()) {
                SystemToast.addOrUpdate(Minecraft.getInstance().getToastManager(), SystemToast.SystemToastId.PERIODIC_NOTIFICATION, Component.nullToEmpty("End of life"), Component.nullToEmpty("This version of Framework reached end of life."));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
