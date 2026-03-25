package de.toxicfox.framework.mixin.sodium;

import de.toxicfox.framework.client.mod.impl.ModXRay;
import de.toxicfox.framework.client.registry.FrameworkRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.DefaultFluidRenderer", remap = false)
public class DefaultFluidRendererMixin {
    @Inject(at = @At("HEAD"), method = "isSideExposed", cancellable = true)
    private void isSideExposed(BlockAndTintGetter world, int x, int y, int z, Direction dir, float height, CallbackInfoReturnable<Boolean> cir) {
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
