package de.glowman554.framework.client.renderer;

import de.glowman554.framework.client.mod.impl.ModTips;
import de.glowman554.framework.client.registry.FrameworkRegistries;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.List;

public class TipRenderer {
    private float tipTimer = 0f;
    private String currentTip;

    public TipRenderer() {
        selectRandomTip();
    }

    public void drawLoadingTips(TextRenderer textRenderer, DrawContext drawContext, int width, int height, float delta) {
        tipTimer += delta;
        if (tipTimer >= 100f) {
            selectRandomTip();
            tipTimer = 0f;
        }
        List<OrderedText> wrappedText = textRenderer.wrapLines(Text.of(currentTip), width / 3);

        int textY = height - textRenderer.fontHeight;
        int textX = 0;

        for (int i = wrappedText.size() - 1; i >= 0; i--) {
            textY = renderTipTextLine(drawContext, wrappedText, textY, textX, i, textRenderer);
        }
        drawContext.drawTextWithShadow(textRenderer, Text.of("Tip:"), textX, textY, 3847130);
    }

    private void selectRandomTip() {
        try {
            ModTips tips = (ModTips) FrameworkRegistries.MODS.get(ModTips.class);
            currentTip = tips.getRandomTip();
        } catch (IllegalArgumentException ignored) {
            currentTip = "<error>";
        }
    }

    private int renderTipTextLine(DrawContext drawContext, List<OrderedText> wrappedText, int textY, int textX, int i, TextRenderer textRenderer) {
        OrderedText orderedText = wrappedText.get(i);
        drawContext.drawTextWithShadow(textRenderer, orderedText, textX, textY, 16777215);
        textY -= (int) (textRenderer.fontHeight * 1.25f);
        return textY;
    }
}
