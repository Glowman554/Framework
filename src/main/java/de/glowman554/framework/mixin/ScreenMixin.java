package de.glowman554.framework.mixin;

import de.glowman554.framework.client.mod.Mod;
import de.glowman554.framework.client.mod.impl.ModMenuBackground;
import de.glowman554.framework.client.registry.FrameworkRegistries;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin {


    @Shadow
    public int width;
    @Shadow
    public int height;
    @Shadow
    @Nullable
    protected MinecraftClient client;
    @Unique
    private Identifier OPTIONS_BACKGROUND_TEXTURE = null;

    @Inject(at = @At("HEAD"), method = "renderDarkening(Lnet/minecraft/client/gui/DrawContext;IIII)V", cancellable = true)
    private void renderDarkening(DrawContext context, int x, int y, int width, int height, CallbackInfo ci) {
        try {

            Mod mod = FrameworkRegistries.MODS.get(ModMenuBackground.class);

            if (mod.isEnabled() && (client == null || client.world == null)) {
                if (OPTIONS_BACKGROUND_TEXTURE == null) {
                    OPTIONS_BACKGROUND_TEXTURE = new Identifier(((ModMenuBackground) mod).getBackground());
                }

                context.setShaderColor(0.25F, 0.25F, 0.25F, 1.0F);
                context.drawTexture(OPTIONS_BACKGROUND_TEXTURE, 0, 0, 0, 0.0F, 0.0F, width, height, 32, 32);
                context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

                ci.cancel();
            }
        } catch (IllegalArgumentException ignored) {
        }
    }
}
