package de.toxicfox.framework.client.screen.config;

import de.toxicfox.framework.client.mod.Mod;
import de.toxicfox.framework.client.screen.ModConfigurationScreen;
import java.lang.reflect.Field;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
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
    public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
        super.extractContent(graphics, mouseX, mouseY, hovered, a);

        checkboxWidget.setX(this.getContentX() + 100);
        checkboxWidget.setY(this.getContentY());
        checkboxWidget.extractRenderState(graphics, mouseX, mouseY, a);
    }
}
