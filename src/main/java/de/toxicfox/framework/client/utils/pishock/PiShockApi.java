package de.toxicfox.framework.client.utils.pishock;

import de.toxicfox.framework.client.utils.WebClient;
import de.toxicfox.framework.client.utils.pishock.data.GetUserDevices;
import de.toxicfox.framework.client.utils.pishock.data.GetUserIfApiKeyValid;
import net.shadew.json.Json;

import java.io.IOException;
import java.util.Map;

public class PiShockApi {
    private final String username;
    private final String apiKey;

    public PiShockApi(String username, String apiKey) {
        this.username = username;
        this.apiKey = apiKey;
    }

    public GetUserIfApiKeyValid getUserIfAPIKeyValid() throws IOException {
        String result = WebClient.get(String.format("https://auth.pishock.com/Auth/GetUserIfAPIKeyValid?apikey=%s&username=%s", apiKey, username), Map.of());

        GetUserIfApiKeyValid object = new GetUserIfApiKeyValid();
        object.fromJSON(Json.json().parse(result));

        return object;
    }

    public GetUserDevices getUserDevices(int userId) throws IOException {
        String result = WebClient.get(String.format("https://ps.pishock.com/PiShock/GetUserDevices?UserId=%s&Token=%s&api=true", userId, apiKey), Map.of());

        GetUserDevices object = new GetUserDevices();
        object.fromJSON(Json.json().parse(result));

        return object;
    }
}
