package de.toxicfox.framework.mixin;

import de.toxicfox.framework.client.mod.impl.ModEntityESP;
import de.toxicfox.framework.client.registry.FrameworkRegistries;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {

    @Unique
    private final HashSet<EntityType<?>> specialTypes = new HashSet<>() {{
        add(EntityType.ITEM);
        add(EntityType.ITEM_FRAME);
        add(EntityType.ITEM_DISPLAY);
        add(EntityType.GLOW_ITEM_FRAME);
        add(EntityType.MARKER);
        add(EntityType.ARMOR_STAND);
    }};

    @Inject(method = "extractRenderState", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/state/EntityRenderState;outlineColor:I", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    public void updateRenderState(T entity, S state, float tickProgress, CallbackInfo ci) {
        try {
            ModEntityESP mod = (ModEntityESP) FrameworkRegistries.MODS.get(ModEntityESP.class);
            if (!mod.isEnabled() || !mod.isColorful()) {
                return;
            }

            if (specialTypes.contains(entity.getType())) {
                state.outlineColor = 0xFF00FFFF;
                return;
            }

            int dangerousDistance = mod.getDangerousDistance();
            int cautionDistance = mod.getCautionDistance();

            int distance = (int) mod.getMc().player.distanceToSqr(entity);

            if (distance < dangerousDistance) {
                state.outlineColor = 0xFFFF0000; // argb
            } else if (distance < cautionDistance) {
                int green = (distance - dangerousDistance) * 255 / (cautionDistance - dangerousDistance);
                state.outlineColor = 0xFFFF0000 | (green << 8);
            } else {
                state.outlineColor = 0xFF00FF00;
            }
        } catch (IllegalArgumentException ignored) {

        }
    }


}
