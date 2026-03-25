package de.toxicfox.framework.mixin.serverpack;

import net.minecraft.server.packs.repository.Pack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Pack.class)
public class ResourcePackProfileMixin {
    @Inject(at = @At("RETURN"), method = "isFixedPosition", cancellable = true)
    private void isPinned(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
