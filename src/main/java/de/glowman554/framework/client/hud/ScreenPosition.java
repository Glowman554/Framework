package de.glowman554.framework.client.hud;


import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;
import net.minecraft.client.MinecraftClient;

public class ScreenPosition extends AutoSavable {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

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
        return (int) (x * mc.getWindow().getScaledWidth());
    }

    public int getAbsoluteY() {
        return (int) (y * mc.getWindow().getScaledHeight());
    }

    public double getRelativeX() {
        return x;
    }

    public double getRelativeY() {
        return y;
    }

    public void setAbsolute(int x, int y) {
        this.x = (double) x / mc.getWindow().getScaledWidth();
        this.y = (double) y / mc.getWindow().getScaledHeight();
    }

    public void setRelative(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
