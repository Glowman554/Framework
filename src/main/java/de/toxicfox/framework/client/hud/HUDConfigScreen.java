package de.toxicfox.framework.client.hud;

import de.toxicfox.framework.client.FrameworkClient;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public class HUDConfigScreen extends Screen {
    private final HashMap<Renderer, ScreenPosition> renderers = new HashMap<>();
    private Optional<Renderer> selectedRenderer = Optional.empty();
    private int prevX, prevY;

    protected HUDConfigScreen(HUDManager api) {
        super(Component.empty());
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
        Minecraft.getInstance().setScreen(new HUDConfigScreen(FrameworkClient.getInstance().getHudManager()));
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        // super.renderBackground(context, mouseX, mouseY, delta);

        this.drawHollowRect(context, 0, 0, this.width - 1, this.height - 1, 0xFFFF0000);

        for (Renderer renderer : renderers.keySet()) {
            ScreenPosition pos = renderers.get(renderer);
            renderer.renderDummy(context, pos);
            this.drawHollowRect(context, pos.getAbsoluteX(), pos.getAbsoluteY(), renderer.getWidth(), renderer.getHeight(), 0xFF00FFFF);
        }
    }

    private void drawHollowRect(GuiGraphics drawContext, int absoluteX, int absoluteY, int width, int height, int color) {
        drawContext.hLine(absoluteX, absoluteX + width, absoluteY, color);
        drawContext.hLine(absoluteX, absoluteX + width, absoluteY + height, color);

        drawContext.vLine(absoluteX, absoluteY + height, absoluteY, color);
        drawContext.vLine(absoluteX + width, absoluteY + height, absoluteY, color);
    }


    @Override
    public boolean mouseDragged(MouseButtonEvent click, double offsetX, double offsetY) {
        if (selectedRenderer.isPresent()) {
            moveSelectedRendererBy((int) click.x() - prevX, (int) click.y() - prevY);
        }

        this.prevX = (int) click.x();
        this.prevY = (int) click.y();

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
        int absoluteX = Math.max(0, Math.min(pos.getAbsoluteX(), Math.max(Minecraft.getInstance().getWindow().getGuiScaledWidth() - renderer.getWidth(), 0)));
        int absoluteY = Math.max(0, Math.min(pos.getAbsoluteY(), Math.max(Minecraft.getInstance().getWindow().getGuiScaledHeight() - renderer.getHeight(), 0)));

        pos.setAbsolute(absoluteX, absoluteY);
    }

    @Override
    public void onClose() {
        super.onClose();
        for (Renderer renderer : renderers.keySet()) {
            renderer.setPos(renderers.get(renderer));
        }
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        this.prevX = (int) click.x();
        this.prevY = (int) click.y();

        loadMouseOver(prevX, prevY);
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