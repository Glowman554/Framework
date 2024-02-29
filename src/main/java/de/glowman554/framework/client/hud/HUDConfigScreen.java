package de.glowman554.framework.client.hud;

import de.glowman554.framework.client.FrameworkClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Predicate;

public class HUDConfigScreen extends Screen {
    private final HashMap<Renderer, ScreenPosition> renderers = new HashMap<>();
    private Optional<Renderer> selectedRenderer = Optional.empty();
    private int prevX, prevY;

    protected HUDConfigScreen(HUDManager api) {
        super(Text.empty());
        Collection<Renderer> registeredRenderers = api.getRegisteredRenderers();

        for (Renderer renderer : registeredRenderers) {
            if (!renderer.isEnabled()) {
                continue;
            }

            ScreenPosition pos = renderer.getPos();

            if (pos == null) {
                pos = ScreenPosition.fromRelativePosition(0.5, 0.5);
                renderer.setPos(pos);
            }

            adjustBounds(renderer, pos);
            this.renderers.put(renderer, pos);
        }
    }

    public static void open() {
        MinecraftClient.getInstance().setScreen(new HUDConfigScreen(FrameworkClient.getInstance().getHudManager()));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);

        this.drawHollowRect(context, 0, 0, this.width - 1, this.height - 1, 0xFFFF0000);

        for (Renderer renderer : renderers.keySet()) {
            ScreenPosition pos = renderers.get(renderer);
            renderer.renderDummy(context, pos);
            this.drawHollowRect(context, pos.getAbsoluteX(), pos.getAbsoluteY(), renderer.getWidth(), renderer.getHeight(), 0xFF00FFFF);
        }
    }

    private void drawHollowRect(DrawContext drawContext, int absoluteX, int absoluteY, int width, int height, int color) {
        drawContext.drawHorizontalLine(absoluteX, absoluteX + width, absoluteY, color);
        drawContext.drawHorizontalLine(absoluteX, absoluteX + width, absoluteY + height, color);

        drawContext.drawVerticalLine(absoluteX, absoluteY + height, absoluteY, color);
        drawContext.drawVerticalLine(absoluteX + width, absoluteY + height, absoluteY, color);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (selectedRenderer.isPresent()) {
            moveSelectedRendererBy((int) mouseX - prevX, (int) mouseY - prevY);
        }

        this.prevX = (int) mouseX;
        this.prevY = (int) mouseY;

        return false;
    }

    private void moveSelectedRendererBy(int x, int y) {
        if (selectedRenderer.isEmpty()) {
            return;
        }
        Renderer renderer = selectedRenderer.get();
        ScreenPosition pos = renderers.get(renderer);

        pos.setAbsolute(pos.getAbsoluteX() + x, pos.getAbsoluteY() + y);

        adjustBounds(renderer, pos);
    }

    private void adjustBounds(Renderer renderer, ScreenPosition pos) {
        int absoluteX = Math.max(0, Math.min(pos.getAbsoluteX(), Math.max(MinecraftClient.getInstance().getWindow().getScaledWidth() - renderer.getWidth(), 0)));
        int absoluteY = Math.max(0, Math.min(pos.getAbsoluteY(), Math.max(MinecraftClient.getInstance().getWindow().getScaledHeight() - renderer.getHeight(), 0)));

        pos.setAbsolute(absoluteX, absoluteY);
    }

    @Override
    public void close() {
        super.close();
        for (Renderer renderer : renderers.keySet()) {
            renderer.setPos(renderers.get(renderer));
        }
    }

    @Override
    public boolean shouldPause() {
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.prevX = (int) mouseX;
        this.prevY = (int) mouseY;

        loadMouseOver((int) mouseX, (int) mouseY);
        return false;
    }

    private void loadMouseOver(int mouseX, int mouseY) {
        this.selectedRenderer = renderers.keySet().stream().filter(new MouseOverFinder(mouseX, mouseY)).findFirst();
    }

    private class MouseOverFinder implements Predicate<Renderer> {

        private final int mouseX;
        private final int mouseY;

        public MouseOverFinder(int mouseX, int mouseY) {
            this.mouseX = mouseX;
            this.mouseY = mouseY;
        }

        @Override
        public boolean test(Renderer iRenderer) {
            ScreenPosition position = renderers.get(iRenderer);

            int absoluteX = position.getAbsoluteX();
            int absoluteY = position.getAbsoluteY();

            if (mouseX >= absoluteX && mouseX <= absoluteX + iRenderer.getWidth()) {
                return mouseY >= absoluteY && mouseY <= absoluteY + iRenderer.getHeight();
            }
            return false;
        }
    }
}