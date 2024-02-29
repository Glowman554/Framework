package de.glowman554.framework.mixin;

import de.glowman554.framework.client.ServerInfoFeatured;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerList.class)
public abstract class ServerListMixin {
    @Shadow
    @Final
    private List<ServerInfo> servers;

    @Shadow
    public abstract void loadFile();

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtIo;read(Ljava/nio/file/Path;)Lnet/minecraft/nbt/NbtCompound;", shift = At.Shift.AFTER), method = "loadFile()V")
    private void loadFileMixin(CallbackInfo info) {
        servers.addAll(ServerInfoFeatured.getFeaturedServers());
    }

    @Inject(at = @At("HEAD"), method = "saveFile()V")
    private void saveFileHead(CallbackInfo info) {
        servers.removeIf(serverInfo -> serverInfo instanceof ServerInfoFeatured);
    }

    @Inject(at = @At("RETURN"), method = "saveFile()V")
    private void saveFileReturn(CallbackInfo info) {
        loadFile();
    }
}
