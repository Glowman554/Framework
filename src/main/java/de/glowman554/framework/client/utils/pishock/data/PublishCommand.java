package de.glowman554.framework.client.utils.pishock.data;

import de.glowman554.config.Savable;
import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;

public class PublishCommand extends AutoSavable {
    @Saved
    public String Target;
    @Saved(remap = Savable.class)
    public PublishCommandBody Body = new PublishCommandBody();

    public static class PublishCommandBody extends AutoSavable {
        @Saved
        public int id;
        @Saved
        public String m;
        @Saved
        public int i;
        @Saved
        public int d;
        @Saved
        public boolean r = false;
        @Saved(remap = Savable.class)
        public L l = new L();

    }

    public static class L extends AutoSavable {
        @Saved
        public int u;
        @Saved
        public String ty = "api";
        @Saved
        public boolean w = false;
        @Saved
        public boolean h = false;
        @Saved
        public String o;
    }

    public static PublishCommand create(String target, int id, int intensity, int duration, int userId, String origin) {
        PublishCommand command = new PublishCommand();
        command.Target = target;
        command.Body.id = id;
        command.Body.m = "s";
        command.Body.i = intensity;
        command.Body.d = duration;
        command.Body.l.u = userId;
        command.Body.l.o = origin;

        return command;
    }
}
