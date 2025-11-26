package de.toxicfox.framework.client;

import de.toxicfox.framework.client.utils.WebClient;
import net.minecraft.MinecraftVersion;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

import java.io.IOException;
import java.util.Map;

public class FrameworkVersionCheck {

    public static void performVersionCheck() {
        String currentVersion = MinecraftVersion.create().name();

        try {
            String result = WebClient.get(FrameworkClient.getInstance().getConfig().development.backend.versionInfo.replace("{version}", currentVersion), Map.of());
            JsonNode root = Json.json().parse(result);

            JsonNode error = root.get("error");
            if (error != null) {
                FrameworkClient.LOGGER.error("Failed to fetch version info: {}", error.asString());
                return;
            }

            if (root.get("endOfLife").asBoolean()) {
                SystemToast.show(MinecraftClient.getInstance().getToastManager(), SystemToast.Type.PERIODIC_NOTIFICATION, Text.of("End of life"), Text.of("This version of Framework reached end of life."));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
