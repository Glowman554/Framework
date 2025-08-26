package de.toxicfox.framework.client.utils.pishock.data;

import de.toxicfox.config.auto.AutoSavable;
import de.toxicfox.config.auto.Saved;
import de.toxicfox.config.auto.processors.SavableProcessor;
import de.toxicfox.config.premade.ArrayListSavable;

public class GetUserIfApiKeyValid extends AutoSavable {
    @Saved
    public int UserId;
    @Saved
    public String Username;
    @Saved
    public String LastLogin;
    @Saved
    public String Password;
    @Saved
    public ArrayListSavable<APIKey> APIKeys = new ArrayListSavable<>(new SavableProcessor(), APIKey::new);

    public static class APIKey extends AutoSavable {
        @Saved
        public int UserAPIKeyId;
        @Saved
        public String APIKey;
        @Saved
        public String Name;
        @Saved
        public String Generated;
        @Saved
        public String Expiry;
    }
}
