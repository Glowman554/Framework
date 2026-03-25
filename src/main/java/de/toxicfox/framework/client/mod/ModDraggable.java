package de.toxicfox.framework.client.mod;

import de.toxicfox.config.Savable;
import de.toxicfox.config.auto.Saved;
import de.toxicfox.framework.client.hud.Renderer;
import de.toxicfox.framework.client.hud.ScreenPosition;

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
        return (textRenderer.lineHeight + 3) * lineNum;
    }
}
