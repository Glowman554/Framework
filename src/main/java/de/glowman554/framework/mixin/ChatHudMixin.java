package de.glowman554.framework.mixin;

import de.glowman554.framework.client.mod.impl.ModQueueNotifier;
import de.glowman554.framework.client.registry.FrameworkRegistries;
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
        try {
            ModQueueNotifier notifier = (ModQueueNotifier) FrameworkRegistries.MODS.get(ModQueueNotifier.class);
            notifier.onChat(message.content().getString());
        } catch (IllegalArgumentException ignored) {
        }
    }
}