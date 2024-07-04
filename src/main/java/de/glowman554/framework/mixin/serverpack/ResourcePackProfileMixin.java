package de.glowman554.framework.mixin.serverpack;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.resource.ResourcePackProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ResourcePackProfile.class)
public class ResourcePackProfileMixin {
    @Inject(at = @At("RETURN"), method = "isPinned", cancellable = true)
    private void isPinned(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
