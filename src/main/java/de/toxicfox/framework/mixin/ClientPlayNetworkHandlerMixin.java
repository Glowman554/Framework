package de.toxicfox.framework.mixin;

import de.toxicfox.framework.client.event.impl.ChatInputEvent;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ClientPacketListener.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "sendChat", at = @At("HEAD"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void sendChatMessage(String content, CallbackInfo ci) {
        ChatInputEvent event = new ChatInputEvent(content);
        event.call();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}

