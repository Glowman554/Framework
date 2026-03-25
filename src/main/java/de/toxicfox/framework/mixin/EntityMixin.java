package de.toxicfox.framework.mixin;

import de.toxicfox.framework.client.mod.impl.ModEntityESP;
import de.toxicfox.framework.client.registry.FrameworkRegistries;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(at = @At("RETURN"), method = "isCurrentlyGlowing", cancellable = true)
    private void isGlowing(CallbackInfoReturnable<Boolean> cir) {
        try {
            if (FrameworkRegistries.MODS.get(ModEntityESP.class).isEnabled()) {
                cir.setReturnValue(true);
            }
        } catch (IllegalArgumentException ignored) {

        }
    }
}
