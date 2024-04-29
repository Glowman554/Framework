package de.glowman554.framework.client.screen;

import de.glowman554.framework.client.FrameworkClient;
import de.glowman554.framework.client.config.Configurable;
import de.glowman554.framework.client.mod.Mod;
import de.glowman554.framework.client.registry.FrameworkRegistries;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.text.Text;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;

public class ModSelectionScreen extends Screen {
    public ModSelectionScreen() {
        super(Text.of("Mod selection"));
    }

    public static void open() {
        MinecraftClient.getInstance().setScreen(new ModSelectionScreen());
    }

    @Override
    protected void init() {
        addDrawableChild(new ModListWidget(MinecraftClient.getInstance(), width, height, 0, 20));
    }

    private static class ModListWidget extends ElementListWidget<ModListEntry> {
        int maxKeyNameLength = 0;

        public ModListWidget(MinecraftClient minecraftClient, int width, int height, int y, int entryHeight) {
            super(minecraftClient, width, height, y, entryHeight);

            List<Mod> mods = FrameworkRegistries.MODS.getRegistry().values().stream().sorted(Comparator.comparing(Mod::getName)).toList();

            for (Mod mod : mods) {
                if (!FrameworkClient.getInstance().getConfig().enableHacks && mod.isHacked()) {
                    continue;
                }

                addEntry(new ModListEntry(mod, this));
                int i = client.textRenderer.getWidth(mod.getName());
                if (i > maxKeyNameLength) {
                    maxKeyNameLength = i;
                }
            }
        }

        @Override
        protected int getScrollbarX() {
            return super.getScrollbarX() + 15;
        }
    }

    private static class ModListEntry extends ElementListWidget.Entry<ModListEntry> {
        private final ButtonWidget toggleButton;
        private final ButtonWidget configButton;
        private final Mod mod;
        private final ModListWidget parent;
        private boolean isConfigurable = false;

        public ModListEntry(Mod mod, ModListWidget parent) {
            this.mod = mod;
            this.parent = parent;

            toggleButton = ButtonWidget.builder(Text.of(getButtonString()), button -> {
                mod.setEnabled(!mod.isEnabled());
                button.setMessage(Text.of(getButtonString()));
                button.setTooltip(Tooltip.of(Text.of(getButtonTooltip())));
            }).tooltip(Tooltip.of(Text.of(getButtonTooltip()))).dimensions(0, 0, 75 + 25, 20).build();

            for (Field field : mod.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Configurable.class)) {
                    isConfigurable = true;
                    break;
                }
            }

            if (isConfigurable) {
                configButton = ButtonWidget.builder(Text.of("⚙"), button -> {
                    ModConfigurationScreen.open(mod);
                }).tooltip(Tooltip.of(Text.of("Configure " + mod.getName().toLowerCase()))).dimensions(0, 0, 20, 20).build();
            } else {
                configButton = null;
            }
        }

        private String getButtonString() {
            if (mod.isEnabled()) {
                return "§2enabled§f";
            } else {
                return "§cdisabled§f";
            }
        }

        private String getButtonTooltip() {
            if (mod.isEnabled()) {
                return "Press button to §cdisable§f the mod";
            } else {
                return "Press button to §2enable§f the mod";
            }
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return List.of();
        }

        @Override
        public List<? extends Element> children() {
            if (isConfigurable) {
                return List.of(toggleButton, configButton);
            } else {
                return List.of(toggleButton);
            }
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            toggleButton.setX(x + 90);
            toggleButton.setY(y);
            toggleButton.render(context, mouseX, mouseY, tickDelta);

            if (isConfigurable) {
                configButton.setX(x + 90 + 75 + 25);
                configButton.setY(y);
                configButton.render(context, mouseX, mouseY, tickDelta);
            }

            String renderName = mod.getName();
            context.drawText(MinecraftClient.getInstance().textRenderer, renderName, x + 90 - parent.maxKeyNameLength, y + entryHeight / 2, -1, true);
        }
    }
}