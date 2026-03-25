package de.toxicfox.framework.client.mod.impl;

import de.toxicfox.framework.client.hud.ScreenPosition;
import de.toxicfox.framework.client.mod.ModDraggable;
import java.util.Arrays;
import java.util.Collections;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public class ModLogo extends ModDraggable {
    private final Identifier logo = Identifier.fromNamespaceAndPath("framework", "icon.png");

    private final String[] message = {
            Minecraft.getInstance().createTitle(),
            "Thank you for using Framework <3",
            "Use M to open the Mod selector list",
            "Use H to open the drag overlay",
            "Type .help in the chat to use commands"
    };

    @Override
    public int getWidth() {
        int size = message.length * textRenderer.lineHeight;
        return size + Collections.max(Arrays.stream(message).map(textRenderer::width).toList()) + 1;
    }

    @Override
    public int getHeight() {
        int size = message.length * textRenderer.lineHeight;
        return Math.max(size, textRenderer.lineHeight * message.length);
    }

    @Override
    public void render(GuiGraphicsExtractor drawContext, ScreenPosition pos) {
        int size = message.length * textRenderer.lineHeight;

        drawContext.blit(RenderPipelines.GUI_TEXTURED, logo, pos.getAbsoluteX(), pos.getAbsoluteY(), 0, 0, size, size, size, size);

        for (int i = 0; i < message.length; i++) {
            drawContext.text(textRenderer, message[i], pos.getAbsoluteX() + size + 1, pos.getAbsoluteY() + 1 + textRenderer.lineHeight * i, -1, true);
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
