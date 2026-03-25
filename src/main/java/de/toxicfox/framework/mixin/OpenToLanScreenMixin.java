package de.toxicfox.framework.mixin;

import de.toxicfox.framework.client.mod.impl.ModForceLANPort;
import de.toxicfox.framework.client.registry.FrameworkRegistries;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.ShareToLanScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShareToLanScreen.class)
public class OpenToLanScreenMixin  {
    @Shadow private int port;

    @Shadow @Nullable private EditBox portEdit;

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
                ((Screen) (Object )this).removeWidget(portEdit);
            }
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Inject(method = "tryParsePort", at = @At(value = "HEAD"), cancellable = true)
    public void updatePort(String portText, CallbackInfoReturnable<Component> cir) {
        try {
            if (FrameworkRegistries.MODS.get(ModForceLANPort.class).isEnabled()) {
                port = getPort();
                cir.setReturnValue(null);
            }
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Unique
    private int getPort() {
        return ((ModForceLANPort) FrameworkRegistries.MODS.get(ModForceLANPort.class)).getPort();
    }

}
