package de.toxicfox.framework.client.hud;


import de.toxicfox.config.auto.AutoSavable;
import de.toxicfox.config.auto.Saved;
import net.minecraft.client.Minecraft;

public class ScreenPosition extends AutoSavable {
    private static final Minecraft mc = Minecraft.getInstance();

    @Saved
    private double x;
    @Saved
    private double y;

    public ScreenPosition(double x, double y) {
        setRelative(x, y);
    }

    public ScreenPosition(int x, int y) {
        setAbsolute(x, y);
    }

    public ScreenPosition() {
    }

    public static ScreenPosition fromRelativePosition(double x, double y) {
        return new ScreenPosition(x, y);
    }

    public static ScreenPosition fromAbsolutePosition(int x, int y) {
        return new ScreenPosition(x, y);
    }

    public int getAbsoluteX() {
        return (int) (x * mc.getWindow().getGuiScaledWidth());
    }

    public int getAbsoluteY() {
        return (int) (y * mc.getWindow().getGuiScaledHeight());
    }

    public double getRelativeX() {
        return x;
    }

    public double getRelativeY() {
        return y;
    }

    public void setAbsolute(int x, int y) {
        this.x = (double) x / mc.getWindow().getGuiScaledWidth();
        this.y = (double) y / mc.getWindow().getGuiScaledHeight();
    }

    public void setRelative(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
