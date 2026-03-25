package de.toxicfox.framework.mixin;

import de.toxicfox.framework.client.event.impl.DeathEvent;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.network.chat.Component;
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
    private Component causeOfDeath;

    @Shadow
    private Component deathScore;

    @Inject(at = @At("RETURN"), method = "init")
    private void init(CallbackInfo ci) {
        if (causeOfDeath != null) {
            new DeathEvent(causeOfDeath.getString(), deathScore.toString()).call();
        } else {
            new DeathEvent(null, null).call();
        }
    }
}
