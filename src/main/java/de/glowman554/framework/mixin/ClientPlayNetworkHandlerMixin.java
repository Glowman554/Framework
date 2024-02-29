package de.glowman554.framework.mixin;

import de.glowman554.framework.client.FrameworkClient;
import de.glowman554.framework.client.command.CommandEvent;
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
        if (content.startsWith(FrameworkClient.getInstance().getConfig().prefix)) {
            try {
                FrameworkClient.getInstance().getCommandManager().onCommand(CommandEvent.from(content));
            } catch (Exception e) {
                e.printStackTrace();
                CommandEvent.sendText("§4Oops: " + e.getMessage());
            }
            ci.cancel();
        }
    }
}

