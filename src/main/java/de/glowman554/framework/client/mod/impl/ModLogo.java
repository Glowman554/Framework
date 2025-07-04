package de.glowman554.framework.client.mod.impl;

import de.glowman554.framework.client.hud.ScreenPosition;
import de.glowman554.framework.client.mod.ModDraggable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.Collections;

public class ModLogo extends ModDraggable {
    private final Identifier logo = Identifier.of("framework", "icon.png");

    private final String[] message = {
            MinecraftClient.getInstance().getWindowTitle(),
            "Thank you for using Framework <3",
            "Use M to open the Mod selector list",
            "Use H to open the drag overlay",
            "Type .help in the chat to use commands"
    };

    @Override
    public int getWidth() {
        int size = message.length * textRenderer.fontHeight;
        return size + Collections.max(Arrays.stream(message).map(textRenderer::getWidth).toList()) + 1;
    }

    @Override
    public int getHeight() {
        int size = message.length * textRenderer.fontHeight;
        return Math.max(size, textRenderer.fontHeight * message.length);
    }

    @Override
    public void render(DrawContext drawContext, ScreenPosition pos) {
        int size = message.length * textRenderer.fontHeight;

        drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, logo, pos.getAbsoluteX(), pos.getAbsoluteY(), 0, 0, size, size, size, size);

        for (int i = 0; i < message.length; i++) {
            drawContext.drawText(textRenderer, message[i], pos.getAbsoluteX() + size + 1, pos.getAbsoluteY() + 1 + textRenderer.fontHeight * i, -1, true);
        }
    }

    @Override
    public boolean defaultEnable() {
        return true;
    }

    @Override
    public String getId() {
        return "logo";
    }

    @Override
    public String getName() {
        return "Logo";
    }

    @Override
    public boolean isHacked() {
        return false;
    }
}
