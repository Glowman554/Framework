package de.glowman554.framework.mixin;


import de.glowman554.framework.client.mod.impl.ModFullBright;
import de.glowman554.framework.client.registry.FrameworkRegistries;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    public abstract Map<StatusEffect, StatusEffectInstance> getActiveStatusEffects();

    @Inject(at = @At("RETURN"), method = "hasStatusEffect", cancellable = true)
    public void hasStatusEffect(StatusEffect effect, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof ClientPlayerEntity) {
            try {
                if (FrameworkRegistries.MODS.get(ModFullBright.class).isEnabled() && effect == StatusEffects.NIGHT_VISION && !getActiveStatusEffects().containsKey(StatusEffects.NIGHT_VISION)) {
                    cir.setReturnValue(true);
                }
            } catch (IllegalArgumentException ignored) {

            }
        }
    }

    @Inject(at = @At("RETURN"), method = "getStatusEffect", cancellable = true)
    public void getStatusEffect(StatusEffect effect, CallbackInfoReturnable<StatusEffectInstance> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof ClientPlayerEntity) {
            try {
                if (FrameworkRegistries.MODS.get(ModFullBright.class).isEnabled() && effect == StatusEffects.NIGHT_VISION && !getActiveStatusEffects().containsKey(StatusEffects.NIGHT_VISION)) {
                    cir.setReturnValue(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 10000, 1));
                }
            } catch (IllegalArgumentException ignored) {

            }
        }
    }
}
