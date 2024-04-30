package de.glowman554.framework.mixin;

import de.glowman554.framework.client.screen.ModSelectionScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
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
    private ButtonWidget modListButton;

    @Inject(at = @At("RETURN"), method = "init")
    private void init(CallbackInfo ci) {
        Screen thisScreen = (Screen) (Object) this;

        modListButton = ButtonWidget.builder(Text.of(BUTTON_STRING), button -> ModSelectionScreen.open()).dimensions(3, 3, 128, 20).build();
        thisScreen.addDrawableChild(modListButton);
    }

    @Inject(at = @At("RETURN"), method = "render")
    private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        modListButton.render(context, mouseX, mouseY, delta);
    }
}
