package de.glowman554.framework.mixin;

import de.glowman554.framework.client.FrameworkClient;
import de.glowman554.framework.client.event.impl.*;
import de.glowman554.framework.client.mod.impl.ModNoTelemetry;
import de.glowman554.framework.client.registry.FrameworkRegistries;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Unique
    private final TickEvent tickEvent = new TickEvent();

    @Inject(at = @At("RETURN"), method = "<init>")
    public void contructor(RunArgs args, CallbackInfo ci) {
        FrameworkClient.getInstance().start();
    }

    @Inject(at = @At("RETURN"), method = "onFinishedLoading")
    private void onFinishedLoading(MinecraftClient.LoadingContext loadingContext, CallbackInfo ci) {
        new ClientFinishLoadingEvent().call();
    }

    @Inject(at = @At("RETURN"), method = "tick()V")
    private void tick(CallbackInfo ci) {
        tickEvent.call();
    }

    @Inject(at = @At("RETURN"), method = "isOptionalTelemetryEnabledByApi", cancellable = true)
    private void isOptionalTelemetryEnabledByApi(CallbackInfoReturnable<Boolean> cir) {
        try {
            if (FrameworkRegistries.MODS.get(ModNoTelemetry.class).isEnabled()) {
                cir.setReturnValue(false);
            }
        } catch (IllegalArgumentException ignored) {

        }
    }

    @Inject(at = @At("RETURN"), method = "isTelemetryEnabledByApi", cancellable = true)
    private void isTelemetryEnabledByApi(CallbackInfoReturnable<Boolean> cir) {
        try {
            if (FrameworkRegistries.MODS.get(ModNoTelemetry.class).isEnabled()) {
                cir.setReturnValue(false);
            }
        } catch (IllegalArgumentException ignored) {

        }
    }

    @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;itemUseCooldown:I", ordinal = 0), method = "doItemUse", cancellable = true)
    private void onDoItemUse(CallbackInfo ci) {
        RightClickEvent event = new RightClickEvent();
        event.call();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }


    @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;crosshairTarget:Lnet/minecraft/util/hit/HitResult;", ordinal = 0), method = "doAttack", cancellable = true)
    private void onDoAttack(CallbackInfoReturnable<Boolean> cir) {
        LeftClickEvent event = new LeftClickEvent(LeftClickEvent.Mode.ATTACK);
        event.call();
        if (event.isCanceled()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/HitResult;getType()Lnet/minecraft/util/hit/HitResult$Type;", ordinal = 0, shift = At.Shift.AFTER), method = "handleBlockBreaking", cancellable = true)
    private void handleBlockBreaking(boolean breaking, CallbackInfo ci) {
        LeftClickEvent event = new LeftClickEvent(LeftClickEvent.Mode.BREAK);
        event.call();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(at = @At("RETURN"), method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V")
    private void disconnect(Screen disconnectionScreen, CallbackInfo ci) {
        new WorldJoinEvent(null, null, null).call();
    }

    @Inject(at = @At("RETURN"), method = "getWindowTitle", cancellable = true)
    private void getWindowTitle(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue("Framework (" + cir.getReturnValue() + ")");
    }

    @Inject(at = @At("HEAD"), method = "stop")
    private void stop(CallbackInfo ci) {
        new ClientStopEvent().call();
    }
}
