package de.glowman554.framework.client.darkloading;

import net.minecraft.resource.ResourceReload;
import net.minecraft.util.Unit;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

import java.util.concurrent.CompletableFuture;

public class FakeResourceReload implements ResourceReload {
    protected final long start;

    protected final long duration;

    public FakeResourceReload(long durationMs) {
        this.start = Util.getMeasuringTimeMs();
        this.duration = durationMs;
    }

    public CompletableFuture<Unit> whenComplete() {
        throw new UnsupportedOperationException();
    }

    public float getProgress() {
        return MathHelper.clamp((float) (Util.getMeasuringTimeMs() - this.start) / (float) this.duration, 0.0F, 1.0F);
    }

    public boolean isComplete() {
        return (Util.getMeasuringTimeMs() - this.start >= this.duration);
    }

    public void throwException() {
    }
}
