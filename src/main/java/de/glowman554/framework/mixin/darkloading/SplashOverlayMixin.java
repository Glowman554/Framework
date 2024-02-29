package de.glowman554.framework.mixin.darkloading;

import com.mojang.blaze3d.systems.RenderSystem;
import de.glowman554.framework.client.darkloading.Constants;
import de.glowman554.framework.client.darkloading.PreviewSplashOverlay;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.IntSupplier;

@Mixin(SplashOverlay.class)
public class SplashOverlayMixin {
    @Shadow
    @Final
    static Identifier LOGO;


    @Mutable
    @Shadow
    @Final
    private static IntSupplier BRAND_ARGB;
    @Unique
    private static boolean initialReloadComplete = false;
    @Shadow
    @Final
    private boolean reloading;
    @Unique
    private boolean skipNextLogoAndBarRendering;

    @Inject(method = {"<clinit>"}, at = {@At("RETURN")})
    private static void adjustBg(CallbackInfo ci) {
        BRAND_ARGB = (Constants.bgRgb::color);
    }

    @Inject(method = {"render"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setOverlay(Lnet/minecraft/client/gui/screen/Overlay;)V")})
    private void onSetOverlay(CallbackInfo info) {
        SplashOverlay splashOverlay = (SplashOverlay) (Object) this;
        if (splashOverlay instanceof PreviewSplashOverlay previewScreen) {
            previewScreen.onRemoved();
        }
    }

    @Inject(method = {"<init>"}, at = {@At("RETURN")})
    private void onInit(CallbackInfo ci) {
        this.skipNextLogoAndBarRendering = !this.reloading;
    }

    @Inject(method = {"render"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/gui/DrawContext;IIF)V")})
    private void onRenderScreen(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!initialReloadComplete) {
            initialReloadComplete = true;
            this.skipNextLogoAndBarRendering = true;
        }
    }

    @Inject(method = {"render"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowWidth()I", ordinal = 2)}, cancellable = true)
    private void onBeforeBeforeLogo(CallbackInfo ci) {
        if (this.skipNextLogoAndBarRendering) {
            ci.cancel();
            this.skipNextLogoAndBarRendering = false;
        }
    }


    @ModifyVariable(method = {"renderProgressBar"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V", ordinal = 0, shift = At.Shift.AFTER), ordinal = 6)
    private int modifyBarBorderColor(int color) {
        return Constants.border | color & 0xFF000000;
    }

    @Inject(method = {"render"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIFFIIII)V", ordinal = 0)})
    private void onBeforeRenderLogo(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        setShaderColorToLogoHighlights();
    }

    @Inject(method = {"render"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIFFIIII)V", ordinal = 1, shift = At.Shift.AFTER)}, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void onAfterRenderLogo(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci, int i, int j, long l, float f, float g, float h, int k, int p, double d, int q, double e, int r) {
        beforeDrawLogoShadows();
        context.drawTexture(LOGO, k - r, p - q, r, (int) d, -0.0625F, 0.0F, 120, 60, 120, 120);
        context.drawTexture(LOGO, k, p - q, r, (int) d, 0.0625F, 60.0F, 120, 60, 120, 120);
        afterDrawLogoShadows();
    }

    @Unique
    private void setShaderColorToLogoHighlights() {
        float[] shaderColor = RenderSystem.getShaderColor();
        shaderColor[0] = Constants.logoRgb.red() - Constants.bgRgb.red();
        shaderColor[1] = Constants.logoRgb.green() - Constants.bgRgb.green();
        shaderColor[2] = Constants.logoRgb.blue() - Constants.bgRgb.blue();
    }

    @Unique
    private void beforeDrawLogoShadows() {
        float[] shaderColor = RenderSystem.getShaderColor();
        shaderColor[0] = shaderColor[0] * -1.0F;
        shaderColor[1] = shaderColor[1] * -1.0F;
        shaderColor[2] = shaderColor[2] * -1.0F;
        RenderSystem.blendEquation(32779);
    }

    @Unique
    private void afterDrawLogoShadows() {
        float[] shaderColor = RenderSystem.getShaderColor();
        shaderColor[0] = 1.0F;
        shaderColor[1] = 1.0F;
        shaderColor[2] = 1.0F;
        RenderSystem.blendEquation(32774);
    }
}
