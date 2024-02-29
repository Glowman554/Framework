package de.glowman554.framework.mixin;

import de.glowman554.framework.client.event.impl.DeathEvent;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeathScreen.class)
public class DeathScreenMixin {
    @Shadow
    @Final
    private Text message;

    @Shadow
    private Text scoreText;

    @Inject(at = @At("RETURN"), method = "init")
    private void init(CallbackInfo ci) {
        if (message != null) {
            new DeathEvent(message.getString(), scoreText.toString()).call();
        } else {
            new DeathEvent(null, null).call();
        }
    }
}
