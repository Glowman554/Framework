package de.toxicfox.framework.mixin;

import de.toxicfox.framework.client.event.impl.ChatEvent;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatComponent.class)
public class ChatHudMixin {
    @Inject(at = @At("HEAD"), method = "logChatMessage")
    private void logChatMessage(GuiMessage message, CallbackInfo ci) {
        new ChatEvent(message).call();
    }
}