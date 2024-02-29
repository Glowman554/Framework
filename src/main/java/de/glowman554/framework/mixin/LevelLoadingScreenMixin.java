package de.glowman554.framework.mixin;

import de.glowman554.framework.client.mod.impl.ModTips;
import de.glowman554.framework.client.registry.FrameworkRegistries;
import de.glowman554.framework.client.renderer.TipRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.LevelLoadingScreen;
import net.minecraft.server.WorldGenerationProgressTracker;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(LevelLoadingScreen.class)
public abstract class LevelLoadingScreenMixin extends Screen {

    @Unique
    private TipRenderer tipRenderer;

    protected LevelLoadingScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void constructor(WorldGenerationProgressTracker worldGenerationProgressTracker, CallbackInfo ci) {
        tipRenderer = new TipRenderer();
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        try {
            if (FrameworkRegistries.MODS.get(ModTips.class).isEnabled()) {
                tipRenderer.drawLoadingTips(textRenderer, context, width, height, delta);
            }
        } catch (IllegalArgumentException ignored) {

        }
    }
}