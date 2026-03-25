package de.toxicfox.framework.mixin;

import de.toxicfox.framework.client.screen.ModSelectionScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Unique
    private static final String BUTTON_STRING = "Open framework mods";
    @Unique
    private Button modListButton;

    @Inject(at = @At("RETURN"), method = "init")
    private void init(CallbackInfo ci) {
        Screen thisScreen = (Screen) (Object) this;

        modListButton = Button.builder(Component.nullToEmpty(BUTTON_STRING), button -> ModSelectionScreen.open()).bounds(3, 3, 128, 20).build();
        thisScreen.addRenderableWidget(modListButton);
    }

    @Inject(at = @At("RETURN"), method = "render")
    private void render(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        modListButton.render(context, mouseX, mouseY, delta);
    }
}
