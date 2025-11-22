package de.toxicfox.framework.client.screen.config;

import de.toxicfox.framework.client.mod.Mod;
import de.toxicfox.framework.client.screen.ModConfigurationScreen;
import de.toxicfox.framework.client.screen.TestButtonExecutor;
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
    public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
        buttonWidget.setX(this.getContentX() + 100);
        buttonWidget.setY(this.getContentY());
        buttonWidget.render(context, mouseX, mouseY, deltaTicks);
    }
}
