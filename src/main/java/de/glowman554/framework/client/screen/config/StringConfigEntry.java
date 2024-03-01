package de.glowman554.framework.client.screen.config;

import de.glowman554.framework.client.mod.Mod;
import de.glowman554.framework.client.screen.ModConfigurationScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.lang.reflect.Field;
import java.util.List;

public class StringConfigEntry extends ModConfigurationScreen.ModConfigEntry {
    private final TextFieldWidget textFieldWidget;

    public StringConfigEntry(Field field, Mod mod, ModConfigurationScreen.ModConfigWidget parent, MinecraftClient client) {
        super(field, mod, parent, client);

        textFieldWidget = new TextFieldWidget(client.textRenderer, 100, 20, Text.empty());
        try {
            textFieldWidget.setText(field.get(mod).toString());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        textFieldWidget.setChangedListener(string -> {
            try {
                field.set(mod, string);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            mod.save();
        });
    }

    @Override
    public List<? extends Element> children() {
        return List.of(textFieldWidget);
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        super.render(context, index, y, x, entryWidth, entryHeight, mouseX, mouseY, hovered, tickDelta);

        textFieldWidget.setX(x + 100);
        textFieldWidget.setY(y);
        textFieldWidget.render(context, mouseX, mouseY, tickDelta);
    }
}