package de.toxicfox.framework.mixin;

import de.toxicfox.framework.client.event.impl.WorldJoinEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.quickplay.QuickPlayLog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(QuickPlayLog.class)
public abstract class QuickPlayLoggerMixin {
    @Unique
    private static final QuickPlayLog new_NOOP = new QuickPlayLog("") {
        public void log(Minecraft client) {
        }

        public void setWorldData(Type worldType, String id, String name) {
            new WorldJoinEvent(worldType, name, id).call();
        }
    };

    @Inject(at = @At("HEAD"), method = "of", cancellable = true)
    private static void create(String relativePath, CallbackInfoReturnable<QuickPlayLog> cir) {
        cir.setReturnValue(relativePath == null ? new_NOOP : new QuickPlayLog(relativePath) {
            public void setWorldData(QuickPlayLog.Type worldType, String id, String name) {
                super.setWorldData(worldType, id, name);
                new WorldJoinEvent(worldType, name, id).call();
            }
        });
    }
}
