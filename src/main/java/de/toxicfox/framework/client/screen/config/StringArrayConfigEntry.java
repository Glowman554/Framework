package de.toxicfox.framework.client.screen.config;

import de.toxicfox.framework.client.config.Configurable;
import de.toxicfox.framework.client.mod.Mod;
import de.toxicfox.framework.client.screen.ModConfigurationScreen;
import java.lang.reflect.Field;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class StringArrayConfigEntry extends ModConfigurationScreen.ModConfigEntry {
    private final Button buttonWidget;

    public StringArrayConfigEntry(Field field, Mod mod, ModConfigurationScreen.ModConfigWidget parent, Minecraft client) {
        super(field, mod, parent, client);
        buttonWidget = Button.builder(Component.nullToEmpty("Open config"), button -> {
            client.setScreen(new StringArrayConfigScreen(this, parent.parent));
        }).tooltip(Tooltip.create(Component.nullToEmpty("Click to open config screen"))).bounds(0, 0, 100, 20).build();
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return List.of(buttonWidget);
    }

    @Override
    public void renderContent(GuiGraphics context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
        super.renderContent(context, mouseX, mouseY, hovered, deltaTicks);

        buttonWidget.setX(this.getContentX() + 100);
        buttonWidget.setY(this.getContentY());
        buttonWidget.render(context, mouseX, mouseY, deltaTicks);
    }

    private static class StringArrayConfigScreen extends Screen {
        private final StringArrayConfigEntry entry;
        private final ModConfigurationScreen parent;

        public StringArrayConfigScreen(StringArrayConfigEntry entry, ModConfigurationScreen parent) {
            super(Component.empty());
            this.entry = entry;
            this.parent = parent;
        }

        @Override
        public void onClose() {
            Minecraft.getInstance().setScreen(parent);
        }


        @Override
        protected void init() {
            super.init();

            MultiLineEditBox editBoxWidget = addRenderableWidget(MultiLineEditBox.builder().setX(0).setY(20).build(font, width, height -40, Component.empty()));

            try {
                StringBuilder text = new StringBuilder();
                for (String entry : (String[]) entry.field.get(entry.mod)) {
                    text.append(entry).append("\n");
                }
                editBoxWidget.setValue(text.toString());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            editBoxWidget.setCharacterLimit(Integer.MAX_VALUE);
            editBoxWidget.setValueListener(string -> {
                try {
                    if (string.isEmpty()) {
                        entry.field.set(entry.mod, new String[]{});
                    } else {
                        entry.field.set(entry.mod, string.split("\\n"));
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                entry.mod.save();
            });

            addRenderableWidget(Button.builder(Component.nullToEmpty("Done"), button -> onClose()).bounds(width / 2 - 50, height - 20, 100, 20).build());
        }

        @Override
        public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
            super.render(context, mouseX, mouseY, delta);
            String text = entry.field.getAnnotation(Configurable.class).text();
            context.drawString(font, text, width / 2 - font.width(text) / 2, 1, -1);
        }
    }
}
