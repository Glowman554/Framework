package de.toxicfox.framework.client.screen.config;

import de.toxicfox.framework.client.mod.Mod;
import de.toxicfox.framework.client.screen.ModConfigurationScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.Text;

import java.lang.reflect.Field;
import java.util.List;

public class BooleanConfigEntry extends ModConfigurationScreen.ModConfigEntry{
    private final CheckboxWidget checkboxWidget;

    public BooleanConfigEntry(Field field, Mod mod, ModConfigurationScreen.ModConfigWidget parent, MinecraftClient client) {
        super(field, mod, parent, client);

        CheckboxWidget.Builder checkboxWidgetBuilder = CheckboxWidget.builder(Text.empty(), client.textRenderer);
        try {
            checkboxWidgetBuilder.checked((boolean) field.get(mod));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        checkboxWidgetBuilder.callback((checkboxWidget, checked) -> {
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
    public List<? extends Element> children() {
        return List.of(checkboxWidget);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
        super.render(context, mouseX, mouseY, hovered, deltaTicks);

        checkboxWidget.setX(this.getContentX() + 100);
        checkboxWidget.setY(this.getContentY());
        checkboxWidget.render(context, mouseX, mouseY, deltaTicks);
    }
}
