package de.toxicfox.framework.mixin;

import de.toxicfox.framework.client.mod.impl.ModAntiBreak;
import de.toxicfox.framework.client.registry.FrameworkRegistries;
import net.minecraft.block.AbstractBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class AbstractBlockStateMixin {

    @Inject(at = @At("RETURN"), method = "getAmbientOcclusionLightLevel", cancellable = true)
    private void getAmbientOcclusionLightLevel(BlockView world, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        try {
            if (FrameworkRegistries.MODS.get(ModAntiBreak.class).isEnabled()) {
                cir.setReturnValue(1F);
            }
        } catch (IllegalArgumentException ignored) {

        }
    }
}
