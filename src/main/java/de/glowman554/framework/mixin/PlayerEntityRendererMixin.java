package de.glowman554.framework.mixin;

import de.glowman554.framework.client.mod.impl.ModHidePlayers;
import de.glowman554.framework.client.registry.FrameworkRegistries;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
    @Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", cancellable = true)
    private void render(AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        try {
            ModHidePlayers mod = (ModHidePlayers) FrameworkRegistries.MODS.get(ModHidePlayers.class);
            if (mod.isHidden(abstractClientPlayerEntity.getUuidAsString())) {
                ci.cancel();
            }
        } catch (IllegalArgumentException ignored) {
        }
    }
}
