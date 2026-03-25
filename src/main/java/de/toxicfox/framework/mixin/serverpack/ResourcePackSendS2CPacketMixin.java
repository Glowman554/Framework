package de.toxicfox.framework.mixin.serverpack;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.common.ClientCommonPacketListener;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;

@Mixin(ClientboundResourcePackPushPacket.class)
public class ResourcePackSendS2CPacketMixin {
    @Shadow
    @Final
    private UUID id;

    @Inject(at = @At("HEAD"), method = "handle(Lnet/minecraft/network/protocol/common/ClientCommonPacketListener;)V", cancellable = true)
    private void apply(ClientCommonPacketListener clientCommonPacketListener, CallbackInfo ci) {
        ServerData serverInfo = Minecraft.getInstance().getCurrentServer();
        if (serverInfo != null && serverInfo.getResourcePackStatus() == ServerData.ServerPackStatus.DISABLED) {
            if (Minecraft.getInstance().getConnection() != null) {
                Minecraft.getInstance().getConnection().send(new ServerboundResourcePackPacket(id, ServerboundResourcePackPacket.Action.SUCCESSFULLY_LOADED));
            }
            ci.cancel();
        }
    }
}
