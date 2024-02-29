package de.glowman554.framework.client.mod.impl;

import de.glowman554.framework.client.hud.ScreenPosition;
import de.glowman554.framework.client.mod.Mod;
import de.glowman554.framework.client.mod.ModDraggable;
import de.glowman554.framework.client.registry.FrameworkRegistries;
import net.minecraft.client.gui.DrawContext;

public class ModModList extends ModDraggable {
    private final String[] dummy = new String[]{"X Ray", "Entity ESP", "Rainbow"};

    @Override
    public int getWidth() {
        return textRenderer.getWidth(dummy[1]);
    }

    @Override
    public int getHeight() {
        return textRenderer.fontHeight * dummy.length;
    }

    @Override
    public void render(DrawContext drawContext, ScreenPosition pos) {
        String[] mods = FrameworkRegistries.MODS.getRegistry().values().stream().filter(Mod::isEnabled).map(Mod::getName).sorted().toArray(String[]::new);
        renderList(drawContext, mods, pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1);
    }

    @Override
    public void renderDummy(DrawContext drawContext, ScreenPosition pos) {
        renderList(drawContext, dummy, pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1);
    }

    private void renderList(DrawContext context, String[] list, int x, int y) {
        for (String entry : list) {
            context.drawText(textRenderer, entry, x, y, -1, true);
            y += textRenderer.fontHeight;
        }
    }

    @Override
    public String getId() {
        return "mod-list";
    }

    @Override
    public String getName() {
        return "Mod list";
    }
}
