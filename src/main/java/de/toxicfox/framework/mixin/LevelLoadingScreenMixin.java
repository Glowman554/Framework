package de.toxicfox.framework.mixin;

import de.toxicfox.framework.client.mod.impl.ModTips;
import de.toxicfox.framework.client.registry.FrameworkRegistries;
import de.toxicfox.framework.client.renderer.TipRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.LevelLoadTracker;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(LevelLoadingScreen.class)
public abstract class LevelLoadingScreenMixin extends Screen {

    @Unique
    private TipRenderer tipRenderer;

    protected LevelLoadingScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void constructor(LevelLoadTracker worldGenerationProgressTracker, LevelLoadingScreen.Reason worldEntryReason, CallbackInfo ci) {
        tipRenderer = new TipRenderer();
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void render(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        try {
            if (FrameworkRegistries.MODS.get(ModTips.class).isEnabled()) {
                tipRenderer.drawLoadingTips(font, context, width, height, delta);
            }
        } catch (IllegalArgumentException ignored) {

        }
    }
}