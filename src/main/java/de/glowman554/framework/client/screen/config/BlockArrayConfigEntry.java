package de.glowman554.framework.client.screen.config;

import de.glowman554.framework.client.config.Configurable;
import de.glowman554.framework.client.mod.Mod;
import de.glowman554.framework.client.screen.ModConfigurationScreen;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EditBoxWidget;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class BlockArrayConfigEntry extends ModConfigurationScreen.ModConfigEntry {
    private final ButtonWidget buttonWidget;

    public BlockArrayConfigEntry(Field field, Mod mod, ModConfigurationScreen.ModConfigWidget parent, MinecraftClient client) {
        super(field, mod, parent, client);
        buttonWidget = ButtonWidget.builder(Text.of("Open config"), button -> {
            client.setScreen(new BlockArrayConfigScreen(this, parent.parent));
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

    private static class BlockArrayConfigScreen extends Screen {
        private final BlockArrayConfigEntry entry;
        private final ModConfigurationScreen parent;

        public BlockArrayConfigScreen(BlockArrayConfigEntry entry, ModConfigurationScreen parent) {
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
                for (Block entry : (Block[]) entry.field.get(entry.mod)) {
                    text.append(Registries.BLOCK.getId(entry)).append("\n");
                }
                editBoxWidget.setText(text.toString());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            editBoxWidget.setMaxLength(Integer.MAX_VALUE);
            editBoxWidget.setChangeListener(string -> {
                try {
                    if (string.isEmpty()) {
                        entry.field.set(entry.mod, new Block[]{});
                    } else {
                        entry.field.set(entry.mod, Arrays.stream(string.split("\\n")).map(s -> Registries.BLOCK.get(new Identifier(s))).toArray(Block[]::new));
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
