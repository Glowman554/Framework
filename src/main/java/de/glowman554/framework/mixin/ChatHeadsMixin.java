package de.glowman554.framework.mixin;

import de.glowman554.framework.client.event.impl.ChatEvent;
import dzwdz.chat_heads.ChatHeads;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.message.MessageType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ChatHeads.class)
public class ChatHeadsMixin {
    @Shadow
    @Nullable
    public static PlayerListEntry lastSender;

    @Inject(at = @At("RETURN"), method = "handleAddedMessage")
    private static void handleAddedMessage(Text message, MessageType.Parameters bound, PlayerListEntry playerInfo, CallbackInfo ci) {
        if (lastSender != null) {
            new ChatEvent(message.getString(), lastSender.getProfile()).call();
        }
    }
}
