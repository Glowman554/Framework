package de.toxicfox.framework.client.screen;

import de.toxicfox.framework.client.FrameworkClient;
import de.toxicfox.framework.client.config.Configurable;
import de.toxicfox.framework.client.mod.Mod;
import de.toxicfox.framework.client.screen.config.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;

public class ModConfigurationScreen extends Screen {
    private final static HashMap<Class<?>, ModConfigEntryCreator> configEntryCreators = new HashMap<>();

    static {
        configEntryCreators.put(int.class, IntegerConfigEntry::new);
        configEntryCreators.put(String.class, StringConfigEntry::new);
        configEntryCreators.put(String[].class, StringArrayConfigEntry::new);
        configEntryCreators.put(Block[].class, BlockArrayConfigEntry::new);
        configEntryCreators.put(TestButtonExecutor.class, TestButtonConfigEntry::new);
        configEntryCreators.put(boolean.class, BooleanConfigEntry::new);
    }

    private final Mod mod;

    public ModConfigurationScreen(Mod mod) {
        super(Component.nullToEmpty(mod.getName()));
        this.mod = mod;
    }

    public static void open(Mod mod) {
        Minecraft.getInstance().setScreen(new ModConfigurationScreen(mod));
    }

    @Override
    protected void init() {
        addRenderableWidget(new ModConfigWidget(mod, Minecraft.getInstance(), width, height, 20, 20, this));
    }


    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);

        String text = mod.getName() + " configuration";
        graphics.text(font, text, width / 2 - font.width(text) / 2, 1, -1);
    }


    public interface ModConfigEntryCreator {
        ModConfigEntry create(Field field, Mod mod, ModConfigWidget parent, Minecraft client);
    }

    public static class ModConfigWidget extends ContainerObjectSelectionList<ModConfigEntry> {
        public final ModConfigurationScreen parent;
        protected int maxKeyNameLength = 0;

        public ModConfigWidget(Mod mod, Minecraft minecraftClient, int width, int height, int y, int entryHeight, ModConfigurationScreen parent) {
            super(minecraftClient, width, height, y, entryHeight);
            this.parent = parent;

            for (Field field : mod.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Configurable.class)) {
                    ModConfigEntryCreator creator = configEntryCreators.get(field.getType());
                    if (creator != null) {
                        addEntry(creator.create(field, mod, this, minecraftClient));
                    } else {
                        FrameworkClient.LOGGER.error("No creator found for {} {}", field, field.getType());
                    }
                }
            }
        }

        @Override
        protected int scrollBarX() {
            return super.scrollBarX() + 15;
        }
    }

    public abstract static class ModConfigEntry extends ContainerObjectSelectionList.Entry<ModConfigEntry> {
        protected final Field field;
        protected final Mod mod;
        private final String title;
        private final ModConfigWidget parent;

        protected ModConfigEntry(Field field, Mod mod, ModConfigWidget parent, Minecraft client) {
            this.field = field;
            this.field.setAccessible(true);
            this.mod = mod;
            title = field.getAnnotation(Configurable.class).text();
            this.parent = parent;

            int i = client.font.width(title);
            if (i > parent.maxKeyNameLength) {
                parent.maxKeyNameLength = i;
            }
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return List.of();
        }

        @Override
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
            graphics.text(Minecraft.getInstance().font, title, this.getContentX() + 90 - parent.maxKeyNameLength, this.getContentY() + getContentHeight() / 2, -1, true);
        }
    }
}
