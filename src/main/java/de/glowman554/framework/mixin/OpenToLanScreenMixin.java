package de.glowman554.framework.mixin;

import de.glowman554.framework.client.mod.impl.ModForceLANPort;
import de.glowman554.framework.client.registry.FrameworkRegistries;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.OpenToLanScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OpenToLanScreen.class)
public class OpenToLanScreenMixin  {
    @Shadow private int port;

    @Shadow @Nullable private TextFieldWidget portField;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void constructor(Screen screen, CallbackInfo ci) {
        try {
            if (FrameworkRegistries.MODS.get(ModForceLANPort.class).isEnabled()) {
                port = getPort();
            }
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Inject(method = "init", at = @At(value = "TAIL"))
    public void init(CallbackInfo ci) {
        try {
            if (FrameworkRegistries.MODS.get(ModForceLANPort.class).isEnabled()) {
                ((Screen) (Object )this).remove(portField);
            }
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Inject(method = "updatePort", at = @At(value = "HEAD"), cancellable = true)
    public void updatePort(String portText, CallbackInfoReturnable<Text> cir) {
        try {
            if (FrameworkRegistries.MODS.get(ModForceLANPort.class).isEnabled()) {
                port = getPort();
                cir.setReturnValue(null);
            }
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V", ordinal = 2), cancellable = true)
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        try {
            if (FrameworkRegistries.MODS.get(ModForceLANPort.class).isEnabled()) {
                ci.cancel();
            }
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Unique
    private int getPort() {
        return ((ModForceLANPort) FrameworkRegistries.MODS.get(ModForceLANPort.class)).getPort();
    }

}
