package de.toxicfox.framework.mixin;

import de.toxicfox.framework.client.event.impl.RenderEvent;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class InGameHudMixin {
    @Inject(method = "render", at = @At(value = "TAIL"))
    public void render(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo callbackInfo) {
        new RenderEvent(context).call();
    }
}
