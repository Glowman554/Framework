package de.toxicfox.framework.client.mod.impl;

import de.toxicfox.framework.client.hud.ScreenPosition;
import de.toxicfox.framework.client.mod.Mod;
import de.toxicfox.framework.client.mod.ModDraggable;
import de.toxicfox.framework.client.registry.FrameworkRegistries;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class ModModList extends ModDraggable {
    private final String[] dummy = new String[]{"X Ray", "Entity ESP", "Rainbow"};

    @Override
    public int getWidth() {
        return textRenderer.width(dummy[1]);
    }

    @Override
    public int getHeight() {
        return textRenderer.lineHeight * dummy.length;
    }

    @Override
    public void render(GuiGraphicsExtractor drawContext, ScreenPosition pos) {
        String[] mods = FrameworkRegistries.MODS.getRegistry().values().stream().filter(Mod::isEnabled).map(Mod::getName).sorted().toArray(String[]::new);
        renderList(drawContext, mods, pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1);
    }

    @Override
    public void renderDummy(GuiGraphicsExtractor drawContext, ScreenPosition pos) {
        renderList(drawContext, dummy, pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1);
    }

    private void renderList(GuiGraphicsExtractor context, String[] list, int x, int y) {
        for (String entry : list) {
            context.text(textRenderer, entry, x, y, -1, true);
            y += textRenderer.lineHeight;
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

    @Override
    public boolean isHacked() {
        return false;
    }
}
