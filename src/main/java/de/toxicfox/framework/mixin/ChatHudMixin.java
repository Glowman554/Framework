package de.toxicfox.framework.mixin;

import de.toxicfox.framework.client.event.impl.ChatEvent;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatHudMixin {
    @Inject(at = @At("HEAD"), method = "logChatMessage")
    private void logChatMessage(ChatHudLine message, CallbackInfo ci) {
        new ChatEvent(message).call();
    }
}