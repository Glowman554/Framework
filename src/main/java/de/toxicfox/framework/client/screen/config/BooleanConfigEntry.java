package de.toxicfox.framework.client.screen.config;

import de.toxicfox.framework.client.mod.Mod;
import de.toxicfox.framework.client.screen.ModConfigurationScreen;
import java.lang.reflect.Field;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;

public class BooleanConfigEntry extends ModConfigurationScreen.ModConfigEntry{
    private final Checkbox checkboxWidget;

    public BooleanConfigEntry(Field field, Mod mod, ModConfigurationScreen.ModConfigWidget parent, Minecraft client) {
        super(field, mod, parent, client);

        Checkbox.Builder checkboxWidgetBuilder = Checkbox.builder(Component.empty(), client.font);
        try {
            checkboxWidgetBuilder.selected((boolean) field.get(mod));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        checkboxWidgetBuilder.onValueChange((checkboxWidget, checked) -> {
            try {
                field.set(mod, checked);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            mod.save();
        });
        checkboxWidget = checkboxWidgetBuilder.build();
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return List.of(checkboxWidget);
    }

    @Override
    public void renderContent(GuiGraphics context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
        super.renderContent(context, mouseX, mouseY, hovered, deltaTicks);

        checkboxWidget.setX(this.getContentX() + 100);
        checkboxWidget.setY(this.getContentY());
        checkboxWidget.render(context, mouseX, mouseY, deltaTicks);
    }
}
