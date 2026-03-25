package de.toxicfox.framework.client.screen.config;

import de.toxicfox.framework.client.mod.Mod;
import de.toxicfox.framework.client.screen.ModConfigurationScreen;
import java.lang.reflect.Field;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;

public class IntegerConfigEntry extends ModConfigurationScreen.ModConfigEntry {
    private final EditBox textFieldWidget;

    public IntegerConfigEntry(Field field, Mod mod, ModConfigurationScreen.ModConfigWidget parent, Minecraft client) {
        super(field, mod, parent, client);

        textFieldWidget = new EditBox(client.font, 100, 20, Component.empty());
        try {
            textFieldWidget.setValue(field.get(mod).toString());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        textFieldWidget.setResponder(string -> {
            try {
                field.set(mod, Integer.parseInt(string));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (NumberFormatException ignored) {
                return;
            }
            mod.save();
        });
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return List.of(textFieldWidget);
    }

    @Override
    public void renderContent(GuiGraphics context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
        super.renderContent(context, mouseX, mouseY, hovered, deltaTicks);

        textFieldWidget.setX(this.getContentX() + 100);
        textFieldWidget.setY(this.getContentY());
        textFieldWidget.render(context, mouseX, mouseY, deltaTicks);
    }
}