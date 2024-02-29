package de.glowman554.framework.client.mod;

import de.glowman554.config.Savable;
import de.glowman554.config.auto.Saved;
import de.glowman554.framework.client.hud.Renderer;
import de.glowman554.framework.client.hud.ScreenPosition;

public abstract class ModDraggable extends Mod implements Renderer {
    @Saved(remap = Savable.class)
    protected ScreenPosition pos = new ScreenPosition();


    public final int getLineOffset(ScreenPosition pos, int lineNum) {
        return pos.getAbsoluteY() + getLineOffset(lineNum);
    }

    @Override
    public ScreenPosition getPos() {
        return pos;
    }

    @Override
    public void setPos(ScreenPosition pos) {
        this.pos = pos;
        save();
    }

    private int getLineOffset(int lineNum) {
        return (textRenderer.fontHeight + 3) * lineNum;
    }
}
