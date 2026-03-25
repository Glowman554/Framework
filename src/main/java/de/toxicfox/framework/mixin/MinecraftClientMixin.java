package de.toxicfox.framework.mixin;

import de.toxicfox.framework.client.FrameworkClient;
import de.toxicfox.framework.client.event.impl.*;
import de.toxicfox.framework.client.mod.impl.ModNoTelemetry;
import de.toxicfox.framework.client.registry.FrameworkRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {
    @Unique
    private final TickEvent tickEvent = new TickEvent();

    @Inject(at = @At("RETURN"), method = "<init>")
    public void contructor(GameConfig args, CallbackInfo ci) {
        FrameworkClient.getInstance().start();
    }

    @Inject(at = @At("RETURN"), method = "onResourceLoadFinished")
    private void onFinishedLoading(Minecraft.GameLoadCookie loadingContext, CallbackInfo ci) {
        new ClientFinishLoadingEvent().call();
    }

    @Inject(at = @At("RETURN"), method = "tick()V")
    private void tick(CallbackInfo ci) {
        tickEvent.call();
    }

    @Inject(at = @At("RETURN"), method = "extraTelemetryAvailable", cancellable = true)
    private void isOptionalTelemetryEnabledByApi(CallbackInfoReturnable<Boolean> cir) {
        try {
            if (FrameworkRegistries.MODS.get(ModNoTelemetry.class).isEnabled()) {
                cir.setReturnValue(false);
            }
        } catch (IllegalArgumentException ignored) {

        }
    }

    @Inject(at = @At("RETURN"), method = "allowsTelemetry", cancellable = true)
    private void isTelemetryEnabledByApi(CallbackInfoReturnable<Boolean> cir) {
        try {
            if (FrameworkRegistries.MODS.get(ModNoTelemetry.class).isEnabled()) {
                cir.setReturnValue(false);
            }
        } catch (IllegalArgumentException ignored) {

        }
    }

    @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;rightClickDelay:I", ordinal = 0), method = "startUseItem", cancellable = true)
    private void onDoItemUse(CallbackInfo ci) {
        RightClickEvent event = new RightClickEvent();
        event.call();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }


    @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;hitResult:Lnet/minecraft/world/phys/HitResult;", ordinal = 0), method = "startAttack", cancellable = true)
    private void onDoAttack(CallbackInfoReturnable<Boolean> cir) {
        LeftClickEvent event = new LeftClickEvent(LeftClickEvent.Mode.ATTACK);
        event.call();
        if (event.isCanceled()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/HitResult;getType()Lnet/minecraft/world/phys/HitResult$Type;", ordinal = 0, shift = At.Shift.AFTER), method = "continueAttack", cancellable = true)
    private void handleBlockBreaking(boolean breaking, CallbackInfo ci) {
        LeftClickEvent event = new LeftClickEvent(LeftClickEvent.Mode.BREAK);
        event.call();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(at = @At("RETURN"), method = "clearDownloadedResourcePacks")
    private void disconnect(CallbackInfo ci) {
        new WorldJoinEvent(null, null, null).call();
    }

    @Inject(at = @At("RETURN"), method = "createTitle", cancellable = true)
    private void getWindowTitle(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue("Framework (" + cir.getReturnValue() + ")");
    }

    @Inject(at = @At("HEAD"), method = "destroy")
    private void stop(CallbackInfo ci) {
        new ClientStopEvent().call();
    }
}
