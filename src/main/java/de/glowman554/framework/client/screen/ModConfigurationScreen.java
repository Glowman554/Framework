package de.glowman554.framework.client.screen;

import de.glowman554.framework.client.FrameworkClient;
import de.glowman554.framework.client.config.Configurable;
import de.glowman554.framework.client.mod.Mod;
import de.glowman554.framework.client.screen.config.BlockArrayConfigEntry;
import de.glowman554.framework.client.screen.config.IntegerConfigEntry;
import de.glowman554.framework.client.screen.config.StringArrayConfigEntry;
import de.glowman554.framework.client.screen.config.StringConfigEntry;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.text.Text;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

public class ModConfigurationScreen extends Screen {
    private final static HashMap<Class<?>, ModConfigEntryCreator> configEntryCreators = new HashMap<>();

    static {
        configEntryCreators.put(int.class, IntegerConfigEntry::new);
        configEntryCreators.put(String.class, StringConfigEntry::new);
        configEntryCreators.put(String[].class, StringArrayConfigEntry::new);
        configEntryCreators.put(Block[].class, BlockArrayConfigEntry::new);
    }

    private final Mod mod;

    public ModConfigurationScreen(Mod mod) {
        super(Text.of(mod.getName()));
        this.mod = mod;
    }

    public static void open(Mod mod) {
        MinecraftClient.getInstance().setScreen(new ModConfigurationScreen(mod));
    }

    @Override
    protected void init() {
        addDrawableChild(new ModConfigWidget(mod, MinecraftClient.getInstance(), width, height, 20, 20, this));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        String text = mod.getName() + " configuration";
        context.drawTextWithShadow(textRenderer, text, width / 2 - textRenderer.getWidth(text) / 2, 1, -1);
    }

    public interface ModConfigEntryCreator {
        ModConfigEntry create(Field field, Mod mod, ModConfigWidget parent, MinecraftClient client);
    }

    public static class ModConfigWidget extends ElementListWidget<ModConfigEntry> {
        public final ModConfigurationScreen parent;
        protected int maxKeyNameLength = 0;

        public ModConfigWidget(Mod mod, MinecraftClient minecraftClient, int width, int height, int y, int entryHeight, ModConfigurationScreen parent) {
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
        protected int getScrollbarX() {
            return super.getScrollbarX() + 15;
        }
    }

    public abstract static class ModConfigEntry extends ElementListWidget.Entry<ModConfigEntry> {
        protected final Field field;
        protected final Mod mod;
        private final String title;
        private final ModConfigWidget parent;

        protected ModConfigEntry(Field field, Mod mod, ModConfigWidget parent, MinecraftClient client) {
            this.field = field;
            this.field.setAccessible(true);
            this.mod = mod;
            title = field.getAnnotation(Configurable.class).text();
            this.parent = parent;

            int i = client.textRenderer.getWidth(title);
            if (i > parent.maxKeyNameLength) {
                parent.maxKeyNameLength = i;
            }
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return List.of();
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            context.drawText(MinecraftClient.getInstance().textRenderer, title, x + 90 - parent.maxKeyNameLength, y + entryHeight / 2, -1, true);
        }
    }
}
