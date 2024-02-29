package de.glowman554.framework.mixin.sodium;

import de.glowman554.framework.client.mod.impl.ModXRay;
import de.glowman554.framework.client.registry.FrameworkRegistries;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.FluidRenderer", remap = false)
public class FluidRendererMixin {
    @Inject(at = @At("HEAD"), method = "isSideExposed", cancellable = true)
    private void isSideExposed(BlockRenderView world, int x, int y, int z, Direction dir, float height, CallbackInfoReturnable<Boolean> cir) {
        BlockPos pos = new BlockPos(x, y, z);
        BlockState state = world.getBlockState(pos);

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
