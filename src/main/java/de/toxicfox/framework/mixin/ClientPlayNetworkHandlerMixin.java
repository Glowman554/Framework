package de.toxicfox.framework.mixin;

import de.toxicfox.framework.client.event.impl.ChatInputEvent;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "sendChatMessage", at = @At("HEAD"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void sendChatMessage(String content, CallbackInfo ci) {
        ChatInputEvent event = new ChatInputEvent(content);
        event.call();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}

