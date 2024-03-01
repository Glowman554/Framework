package de.glowman554.framework.mixin;

import de.glowman554.framework.client.FrameworkClient;
import de.glowman554.framework.client.event.impl.WorldJoinEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.QuickPlayLogger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(QuickPlayLogger.class)
public abstract class QuickPlayLoggerMixin {
    @Unique
    private static final QuickPlayLogger new_NOOP = new QuickPlayLogger("") {
        public void save(MinecraftClient client) {
        }

        public void setWorld(WorldType worldType, String id, String name) {
            new WorldJoinEvent(worldType, name, id).call();
        }
    };

    @Inject(at = @At("HEAD"), method = "create", cancellable = true)
    private static void create(String relativePath, CallbackInfoReturnable<QuickPlayLogger> cir) {
        cir.setReturnValue(relativePath == null ? new_NOOP : new QuickPlayLogger(relativePath) {
            public void setWorld(QuickPlayLogger.WorldType worldType, String id, String name) {
                super.setWorld(worldType, id, name);
                new WorldJoinEvent(worldType, name, id).call();
            }
        });
    }
}
