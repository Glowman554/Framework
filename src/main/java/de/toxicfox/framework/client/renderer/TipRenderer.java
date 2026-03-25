package de.toxicfox.framework.client.renderer;

import de.toxicfox.framework.client.mod.impl.ModTips;
import de.toxicfox.framework.client.registry.FrameworkRegistries;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public class TipRenderer {
    private float tipTimer = 0f;
    private String currentTip;

    public TipRenderer() {
        selectRandomTip();
    }

    public void drawLoadingTips(Font textRenderer, GuiGraphics drawContext, int width, int height, float delta) {
        tipTimer += delta;
        if (tipTimer >= 100f) {
            selectRandomTip();
            tipTimer = 0f;
        }
        List<FormattedCharSequence> wrappedText = textRenderer.split(Component.nullToEmpty(currentTip), width / 3);

        int textY = height - textRenderer.lineHeight;
        int textX = 0;

        for (int i = wrappedText.size() - 1; i >= 0; i--) {
            textY = renderTipTextLine(drawContext, wrappedText, textY, textX, i, textRenderer);
        }
        drawContext.drawString(textRenderer, Component.nullToEmpty("Tip:"), textX, textY, 3847130);
    }

    private void selectRandomTip() {
        try {
            ModTips tips = (ModTips) FrameworkRegistries.MODS.get(ModTips.class);
            currentTip = tips.getRandomTip();
        } catch (IllegalArgumentException ignored) {
            currentTip = "<error>";
        }
    }

    private int renderTipTextLine(GuiGraphics drawContext, List<FormattedCharSequence> wrappedText, int textY, int textX, int i, Font textRenderer) {
        FormattedCharSequence orderedText = wrappedText.get(i);
        drawContext.drawString(textRenderer, orderedText, textX, textY, 16777215);
        textY -= (int) (textRenderer.lineHeight * 1.25f);
        return textY;
    }
}
