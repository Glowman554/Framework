package de.toxicfox.framework.client.darkloading;

import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.util.Util;
import java.util.concurrent.CompletableFuture;

public class FakeResourceReload implements ReloadInstance {
    protected final long start;

    protected final long duration;

    public FakeResourceReload(long durationMs) {
        this.start = Util.getMillis();
        this.duration = durationMs;
    }

    public CompletableFuture<Unit> done() {
        throw new UnsupportedOperationException();
    }

    public float getActualProgress() {
        return Mth.clamp((float) (Util.getMillis() - this.start) / (float) this.duration, 0.0F, 1.0F);
    }

    public boolean isDone() {
        return (Util.getMillis() - this.start >= this.duration);
    }

    public void checkExceptions() {
    }
}
