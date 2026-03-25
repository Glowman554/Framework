package de.toxicfox.framework.mixin;


import de.toxicfox.framework.client.mod.impl.ModFullBright;
import de.toxicfox.framework.client.registry.FrameworkRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    public abstract Map<MobEffect, MobEffectInstance> getActiveEffectsMap();

    @Inject(at = @At("RETURN"), method = "hasEffect", cancellable = true)
    public void hasStatusEffect(Holder<MobEffect> effect, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof LocalPlayer) {
            try {
                if (FrameworkRegistries.MODS.get(ModFullBright.class).isEnabled() && effect == MobEffects.NIGHT_VISION && !getActiveEffectsMap().containsKey(MobEffects.NIGHT_VISION)) {
                    cir.setReturnValue(true);
                }
            } catch (IllegalArgumentException ignored) {

            }
        }
    }

    @Inject(at = @At("RETURN"), method = "getEffect", cancellable = true)
    public void getStatusEffect(Holder<MobEffect> effect, CallbackInfoReturnable<MobEffectInstance> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof LocalPlayer) {
            try {
                if (FrameworkRegistries.MODS.get(ModFullBright.class).isEnabled() && effect == MobEffects.NIGHT_VISION && !getActiveEffectsMap().containsKey(MobEffects.NIGHT_VISION)) {
                    cir.setReturnValue(new MobEffectInstance(MobEffects.NIGHT_VISION, 10000, 1));
                }
            } catch (IllegalArgumentException ignored) {

            }
        }
    }
}
