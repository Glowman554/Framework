package de.glowman554.framework.client.screen.config;

import de.glowman554.framework.client.mod.Mod;
import de.glowman554.framework.client.screen.ModConfigurationScreen;
import de.glowman554.framework.client.screen.TestButtonExecutor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.lang.reflect.Field;
import java.util.List;

public class TestButtonConfigEntry extends ModConfigurationScreen.ModConfigEntry{
    private final ButtonWidget buttonWidget;

    public TestButtonConfigEntry(Field field, Mod mod, ModConfigurationScreen.ModConfigWidget parent, MinecraftClient client) {
        super(field, mod, parent, client);
        buttonWidget = ButtonWidget.builder(Text.of("Test"), button -> {
            try {
                TestButtonExecutor executor = (TestButtonExecutor) field.get(mod);
                executor.trigger();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).tooltip(Tooltip.of(Text.of("Click to run test"))).dimensions(0, 0, 100, 20).build();
    }

    @Override
    public List<? extends Element> children() {
        return List.of(buttonWidget);
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        super.render(context, index, y, x, entryWidth, entryHeight, mouseX, mouseY, hovered, tickDelta);

        buttonWidget.setX(x + 100);
        buttonWidget.setY(y);
        buttonWidget.render(context, mouseX, mouseY, tickDelta);
    }
}
