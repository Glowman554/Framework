package de.glowman554.framework.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import de.glowman554.framework.client.mod.impl.ModEntityESP;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.HashSet;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Unique
    private final HashSet<EntityType<?>> specialTypes = new HashSet<>() {{
        add(EntityType.ITEM);
        add(EntityType.ITEM_FRAME);
        add(EntityType.ITEM_DISPLAY);
        add(EntityType.GLOW_ITEM_FRAME);
        add(EntityType.MARKER);
        add(EntityType.ARMOR_STAND);
    }};

    @ModifyArgs(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/OutlineVertexConsumerProvider;setColor(IIII)V"))
    private void renderEntities(Args args, @Local Entity entity) {
        try {
            ModEntityESP mod = (ModEntityESP) de.glowman554.framework.client.registry.FrameworkRegistries.MODS.get(ModEntityESP.class);
            if (!mod.isEnabled() || !mod.isColorful()) {
                return;
            }

            if (specialTypes.contains(entity.getType())) {
                args.set(0, 0);
                args.set(1, 255);
                args.set(2, 255);
                args.set(3, 255);
                return;
            }

            int dangerousDistance = mod.getDangerousDistance();
            int cautionDistance = mod.getCautionDistance();

            int distance = (int) mod.getMc().player.squaredDistanceTo(entity);

            if (distance < dangerousDistance) {
                args.set(0, 255);
                args.set(1, 0);
                args.set(2, 0);
            } else if (distance < cautionDistance) {
                int green = (distance - dangerousDistance) * 255 / (cautionDistance - dangerousDistance);
                args.set(0, 255);
                args.set(1, green);
                args.set(2, 0);
            } else {
                args.set(0, 0);
                args.set(1, 255);
                args.set(2, 0);
            }

            args.set(3, 255);
        } catch (IllegalArgumentException ignored) {

        }
    }

}
