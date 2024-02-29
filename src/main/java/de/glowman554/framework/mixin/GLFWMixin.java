package de.glowman554.framework.mixin;

import de.glowman554.framework.client.event.impl.WindowOpeningEvent;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GLFW.class)
public class GLFWMixin {
    @Inject(at = @At("HEAD"), method = "glfwCreateWindow(IILjava/lang/CharSequence;JJ)J", remap = false)
    private static void glfwCreateWindow(int width, int height, CharSequence title, long monitor, long share, CallbackInfoReturnable<Long> cir) {
        new WindowOpeningEvent().call();
    }
}
