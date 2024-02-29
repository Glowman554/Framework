package de.glowman554.framework.mixin;

import de.glowman554.framework.client.mod.impl.ModXRay;
import de.glowman554.framework.client.registry.FrameworkRegistries;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BasicBakedModel;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BasicBakedModel.class)
public class BasicBakedModelMixin {
    @Inject(at = @At("RETURN"), method = "getQuads", cancellable = true)
    private void getQuads(BlockState state, Direction face, Random random, CallbackInfoReturnable<List<BakedQuad>> cir) {
        try {
            ModXRay mod = (ModXRay) FrameworkRegistries.MODS.get(ModXRay.class);
            if (mod.isEnabled() && state != null) {
                if (!mod.check(state.getBlock())) {
                    cir.setReturnValue(List.of());
                }
            }
        } catch (IllegalArgumentException ignored) {

        }
    }
}
