package de.toxicfox.framework.mixin;

import de.toxicfox.framework.client.event.impl.ClientPlayerTickEvent;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class ClientPlayerEntityMixin {
    @Unique
    private final ClientPlayerTickEvent event = new ClientPlayerTickEvent();

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;tick()V"), method = "tick")
    private void onTick(CallbackInfo ci) {
        event.call();
    }
}
