package de.glowman554.framework.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import de.glowman554.framework.client.mod.impl.ModRainbowText;
import de.glowman554.framework.client.registry.FrameworkRegistries;
import net.minecraft.text.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(targets = "net.minecraft.client.font.TextRenderer$Drawer")
public class TextRendererMixin {
    @Inject(at = @At("HEAD"), method = "accept")
    public void accept(CallbackInfoReturnable<Boolean> cir, @Local LocalRef<Style> styleRef) {
        try {
            ModRainbowText mod = (ModRainbowText) FrameworkRegistries.MODS.get(ModRainbowText.class);
            if (mod.isEnabled()) {
                Color c = Color.getHSBColor(mod.getHue(), 1, 1);

                Style style = styleRef.get();
                style = style.withColor(c.getRGB());
                styleRef.set(style);
            }
        } catch (IllegalArgumentException ignored) {

        }
    }
}
