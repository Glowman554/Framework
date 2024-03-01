package de.glowman554.framework.client.screen.config;

import de.glowman554.framework.client.config.Configurable;
import de.glowman554.framework.client.mod.Mod;
import de.glowman554.framework.client.screen.ModConfigurationScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EditBoxWidget;
import net.minecraft.text.Text;

import java.lang.reflect.Field;
import java.util.List;

public class StringArrayConfigEntry extends ModConfigurationScreen.ModConfigEntry {
    private final ButtonWidget buttonWidget;

    public StringArrayConfigEntry(Field field, Mod mod, ModConfigurationScreen.ModConfigWidget parent, MinecraftClient client) {
        super(field, mod, parent, client);
        buttonWidget = ButtonWidget.builder(Text.of("Open config"), button -> {
            client.setScreen(new StringArrayConfigScreen(this, parent.parent));
        }).tooltip(Tooltip.of(Text.of("Click to open config screen"))).dimensions(0, 0, 100, 20).build();
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

    private static class StringArrayConfigScreen extends Screen {
        private final StringArrayConfigEntry entry;
        private final ModConfigurationScreen parent;

        public StringArrayConfigScreen(StringArrayConfigEntry entry, ModConfigurationScreen parent) {
            super(Text.empty());
            this.entry = entry;
            this.parent = parent;
        }

        @Override
        public void close() {
            MinecraftClient.getInstance().setScreen(parent);
        }


        @Override
        protected void init() {
            super.init();

            EditBoxWidget editBoxWidget = addDrawableChild(new EditBoxWidget(textRenderer, 0, 20, width, height - 40, Text.empty(), Text.empty()));

            try {
                StringBuilder text = new StringBuilder();
                for (String entry : (String[]) entry.field.get(entry.mod)) {
                    text.append(entry).append("\n");
                }
                editBoxWidget.setText(text.toString());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            editBoxWidget.setMaxLength(Integer.MAX_VALUE);
            editBoxWidget.setChangeListener(string -> {
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

            addDrawableChild(ButtonWidget.builder(Text.of("Done"), button -> close()).dimensions(width / 2 - 50, height - 20, 100, 20).build());
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            super.render(context, mouseX, mouseY, delta);
            String text = entry.field.getAnnotation(Configurable.class).text();
            context.drawTextWithShadow(textRenderer, text, width / 2 - textRenderer.getWidth(text) / 2, 1, -1);
        }
    }
}
