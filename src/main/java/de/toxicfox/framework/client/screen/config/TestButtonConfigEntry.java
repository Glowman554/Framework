package de.toxicfox.framework.client.screen.config;

import de.toxicfox.framework.client.mod.Mod;
import de.toxicfox.framework.client.screen.ModConfigurationScreen;
import de.toxicfox.framework.client.screen.TestButtonExecutor;
import java.lang.reflect.Field;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;

public class TestButtonConfigEntry extends ModConfigurationScreen.ModConfigEntry{
    private final Button buttonWidget;

    public TestButtonConfigEntry(Field field, Mod mod, ModConfigurationScreen.ModConfigWidget parent, Minecraft client) {
        super(field, mod, parent, client);
        buttonWidget = Button.builder(Component.nullToEmpty("Test"), button -> {
            try {
                TestButtonExecutor executor = (TestButtonExecutor) field.get(mod);
                executor.trigger();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).tooltip(Tooltip.create(Component.nullToEmpty("Click to run test"))).bounds(0, 0, 100, 20).build();
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return List.of(buttonWidget);
    }


    @Override
    public void renderContent(GuiGraphics context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
        buttonWidget.setX(this.getContentX() + 100);
        buttonWidget.setY(this.getContentY());
        buttonWidget.render(context, mouseX, mouseY, deltaTicks);
    }
}
