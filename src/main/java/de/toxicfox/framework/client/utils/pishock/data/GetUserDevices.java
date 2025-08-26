package de.toxicfox.framework.client.utils.pishock.data;

import de.toxicfox.config.Savable;
import de.toxicfox.config.auto.AutoSavable;
import de.toxicfox.config.auto.Saved;
import de.toxicfox.config.auto.processors.SavableProcessor;
import de.toxicfox.config.premade.ArrayListSavable;
import net.shadew.json.JsonNode;


public class GetUserDevices implements Savable {
    public ArrayListSavable<UserDevice> devices = new ArrayListSavable<>(new SavableProcessor(), UserDevice::new);

    @Override
    public void fromJSON(JsonNode jsonNode) {
        devices.fromJSON(jsonNode);
    }

    @Override
    public JsonNode toJSON() {
        return devices.toJSON();
    }

    public static class UserDevice extends AutoSavable {
        @Saved
        public int clientId;
        @Saved
        public String name;
        @Saved
        public int userId;
        @Saved
        public String username;
        @Saved
        public ArrayListSavable<Shocker> shockers = new ArrayListSavable<>(new SavableProcessor(), Shocker::new);
    }

    public static class Shocker extends AutoSavable {
        @Saved
        public String name;
        @Saved
        public int shockerId;
        @Saved
        public boolean isPaused;
    }
}
