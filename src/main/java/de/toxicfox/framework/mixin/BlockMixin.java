package de.toxicfox.framework.mixin;

import de.toxicfox.framework.client.mod.impl.ModXRay;
import de.toxicfox.framework.client.registry.FrameworkRegistries;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(at = @At("HEAD"), method = "shouldRenderFace", cancellable = true)
    private static void onShouldDrawSide(BlockState state, BlockState otherState, Direction side, CallbackInfoReturnable<Boolean> cir) {
        try {
            ModXRay mod = (ModXRay) FrameworkRegistries.MODS.get(ModXRay.class);
            if (mod.isEnabled()) {
                if (mod.check(state.getBlock())) {
                    cir.setReturnValue(true);
                } else {
                    cir.setReturnValue(false);
                }
            }
        } catch (IllegalArgumentException ignored) {

        }
    }
}
