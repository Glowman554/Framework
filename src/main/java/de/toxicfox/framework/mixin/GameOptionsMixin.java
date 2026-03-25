package de.toxicfox.framework.mixin;

import com.google.common.collect.Lists;
import de.toxicfox.framework.client.FrameworkKeyBinding;
import de.toxicfox.framework.client.registry.FrameworkRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;

@Mixin(Options.class)
public class GameOptionsMixin {

    @Final
    @Shadow
    @Mutable
    public KeyMapping[] keyMappings;

    @Inject(at = @At("HEAD"), method = "load")
    public void load(CallbackInfo ci) {
        List<FrameworkKeyBinding> instances = List.of(FrameworkRegistries.KEY_BINDINGS.getRegistry().values().toArray(new FrameworkKeyBinding[0]));

        List<KeyMapping> newKeysAll = Lists.newArrayList(keyMappings);
        newKeysAll.removeAll(instances);
        newKeysAll.addAll(instances);
        keyMappings = newKeysAll.toArray(new KeyMapping[0]);
    }
}