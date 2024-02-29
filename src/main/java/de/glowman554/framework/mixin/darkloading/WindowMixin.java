package de.glowman554.framework.mixin.darkloading;

import de.glowman554.framework.client.darkloading.Constants;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLCapabilities;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({Window.class})
public abstract class WindowMixin {
    @Shadow
    @Final
    private long handle;

    @Redirect(method = {"<init>"}, at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL;createCapabilities()Lorg/lwjgl/opengl/GLCapabilities;", remap = false))
    private GLCapabilities onCreateCapabilities() {
        GLFW.glfwSwapBuffers(handle);
        GLCapabilities result = GL.createCapabilities();
        GL11.glClearColor(Constants.bgRgb.red(), Constants.bgRgb.green(), Constants.bgRgb.blue(), 1.0F);
        GL11.glClear(16640);
        GLFW.glfwSwapBuffers(handle);
        return result;
    }
}
