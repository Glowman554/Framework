package de.toxicfox.framework.mixin;

import de.toxicfox.framework.client.mod.impl.ModAntiBreak;
import de.toxicfox.framework.client.registry.FrameworkRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public class AbstractBlockStateMixin {

    @Inject(at = @At("RETURN"), method = "getShadeBrightness", cancellable = true)
    private void getAmbientOcclusionLightLevel(BlockGetter world, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        try {
            if (FrameworkRegistries.MODS.get(ModAntiBreak.class).isEnabled()) {
                cir.setReturnValue(1F);
            }
        } catch (IllegalArgumentException ignored) {

        }
    }
}
