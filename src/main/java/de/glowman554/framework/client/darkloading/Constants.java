package de.glowman554.framework.client.darkloading;

public class Constants {
    public static final int bar;
    public static final int barBg;
    public static final int border;
    public static final RgbColor logoRgb;
    public static final RgbColor bgRgb;

    static {
        bgRgb = new RgbColor(1316892);
        logoRgb = new RgbColor(16777215);
        border = 3158838;
        barBg = 1316892;
        bar = 14821431;
    }

    public record RgbColor(int color) {

        public float red() {
            return getChannel(16);
        }

        public float green() {
            return getChannel(8);
        }

        public float blue() {
            return getChannel(0);
        }

        private float getChannel(int offset) {
            return (color >> offset & 0xFF) / 255.0F;
        }
    }
}
