package de.toxicfox.framework.client.screen;

import de.toxicfox.framework.client.FrameworkClient;
import de.toxicfox.framework.client.config.Configurable;
import de.toxicfox.framework.client.mod.Mod;
import de.toxicfox.framework.client.registry.FrameworkRegistries;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ModSelectionScreen extends Screen {
    public ModSelectionScreen() {
        super(Component.nullToEmpty("Mod selection"));
    }

    public static void open() {
        Minecraft.getInstance().setScreen(new ModSelectionScreen());
    }

    @Override
    protected void init() {
        addRenderableWidget(new ModListWidget(Minecraft.getInstance(), width, height, 0, 20));
    }

    private static class ModListWidget extends ContainerObjectSelectionList<ModListEntry> {
        int maxKeyNameLength = 0;

        public ModListWidget(Minecraft minecraftClient, int width, int height, int y, int entryHeight) {
            super(minecraftClient, width, height, y, entryHeight);

            List<Mod> mods = FrameworkRegistries.MODS.getRegistry().values().stream().sorted(Comparator.comparing(Mod::getName)).toList();

            for (Mod mod : mods) {
                if (!FrameworkClient.getInstance().getConfig().enableHacks && mod.isHacked()) {
                    continue;
                }

                addEntry(new ModListEntry(mod, this));
                int i = minecraft.font.width(mod.getName());
                if (i > maxKeyNameLength) {
                    maxKeyNameLength = i;
                }
            }
        }

        @Override
        protected int scrollBarX() {
            return super.scrollBarX() + 15;
        }
    }

    private static class ModListEntry extends ContainerObjectSelectionList.Entry<ModListEntry> {
        private final Button toggleButton;
        private final Button configButton;
        private final Mod mod;
        private final ModListWidget parent;
        private boolean isConfigurable = false;

        public ModListEntry(Mod mod, ModListWidget parent) {
            this.mod = mod;
            this.parent = parent;

            toggleButton = Button.builder(Component.nullToEmpty(getButtonString()), button -> {
                mod.setEnabled(!mod.isEnabled());
                button.setMessage(Component.nullToEmpty(getButtonString()));
                button.setTooltip(Tooltip.create(Component.nullToEmpty(getButtonTooltip())));
            }).tooltip(Tooltip.create(Component.nullToEmpty(getButtonTooltip()))).bounds(0, 0, 75 + 25, 20).build();

            for (Field field : mod.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Configurable.class)) {
                    isConfigurable = true;
                    break;
                }
            }

            if (isConfigurable) {
                configButton = Button.builder(Component.nullToEmpty("⚙"), button -> {
                    ModConfigurationScreen.open(mod);
                }).tooltip(Tooltip.create(Component.nullToEmpty("Configure " + mod.getName().toLowerCase()))).bounds(0, 0, 20, 20).build();
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
        public List<? extends NarratableEntry> narratables() {
            return List.of();
        }

        @Override
        public List<? extends GuiEventListener> children() {
            if (isConfigurable) {
                return List.of(toggleButton, configButton);
            } else {
                return List.of(toggleButton);
            }
        }

        @Override
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
            toggleButton.setX(this.getContentX() + 90);
            toggleButton.setY(this.getContentY());
            toggleButton.extractRenderState(graphics, mouseX, mouseY, a);

            if (isConfigurable) {
                configButton.setX(this.getContentX() + 90 + 75 + 25);
                configButton.setY(this.getContentY());
                configButton.extractRenderState(graphics, mouseX, mouseY, a);
            }

            String renderName = mod.getName();
            graphics.text(Minecraft.getInstance().font, renderName, this.getContentX() + 90 - parent.maxKeyNameLength, this.getContentY() + getContentHeight() / 2, -1, true);
        }
    }
}