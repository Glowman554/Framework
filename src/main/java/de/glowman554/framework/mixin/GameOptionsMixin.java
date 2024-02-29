package de.glowman554.framework.mixin;

import com.google.common.collect.Lists;
import de.glowman554.framework.client.FrameworkKeyBinding;
import de.glowman554.framework.client.registry.FrameworkRegistries;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GameOptions.class)
public class GameOptionsMixin {

    @Final
    @Shadow
    @Mutable
    public KeyBinding[] allKeys;

    @Inject(at = @At("HEAD"), method = "load")
    public void load(CallbackInfo ci) {
        List<FrameworkKeyBinding> instances = List.of(FrameworkRegistries.KEY_BINDINGS.getRegistry().values().toArray(new FrameworkKeyBinding[0]));

        List<KeyBinding> newKeysAll = Lists.newArrayList(allKeys);
        newKeysAll.removeAll(instances);
        newKeysAll.addAll(instances);
        allKeys = newKeysAll.toArray(new KeyBinding[0]);
    }
}