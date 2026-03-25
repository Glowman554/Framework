package de.toxicfox.framework.mixin;

import de.toxicfox.framework.client.ServerInfoFeatured;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;

@Mixin(ServerList.class)
public abstract class ServerListMixin {
    @Shadow
    @Final
    private List<ServerData> serverList;

    @Shadow
    public abstract void load();

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtIo;read(Ljava/nio/file/Path;)Lnet/minecraft/nbt/CompoundTag;", shift = At.Shift.AFTER), method = "load()V")
    private void loadFileMixin(CallbackInfo info) {
        serverList.addAll(ServerInfoFeatured.getFeaturedServers());
    }

    @Inject(at = @At("HEAD"), method = "save()V")
    private void saveFileHead(CallbackInfo info) {
        serverList.removeIf(serverInfo -> serverInfo instanceof ServerInfoFeatured);
    }

    @Inject(at = @At("RETURN"), method = "save()V")
    private void saveFileReturn(CallbackInfo info) {
        load();
    }
}
