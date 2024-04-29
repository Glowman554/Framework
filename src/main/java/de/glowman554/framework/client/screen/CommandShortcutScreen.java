package de.glowman554.framework.client.screen;

import com.google.common.collect.ImmutableList;
import de.glowman554.framework.client.FrameworkClient;
import de.glowman554.framework.client.commandshortcuts.CommandShortcut;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.text.Text;

import java.util.List;

public class CommandShortcutScreen extends Screen {


    public CommandShortcutScreen() {
        super(Text.of("Command shortcuts"));

    }

    public static void open() {
        MinecraftClient.getInstance().setScreen(new CommandShortcutScreen());
    }

    @Override
    protected void init() {
        addDrawableChild(new CommandShortcutWidget(MinecraftClient.getInstance(), width, height, 0, 20, FrameworkClient.getInstance().getCommandShortcutsManager().getCommandShortcutConfig().shortcuts, this));
    }

    private static class CommandShortcutWidget extends ElementListWidget<CommandShortcutEntry> {
        public CommandShortcutWidget(MinecraftClient minecraftClient, int width, int height, int y, int entryHeight, CommandShortcut[] shortcuts, CommandShortcutScreen commandShortcutScreen) {
            super(minecraftClient, width, height, y, entryHeight);

            for (CommandShortcut shortcut : shortcuts) {
                addEntry(new CommandShortcutEntry(shortcut, commandShortcutScreen));
            }
        }

        @Override
        protected int getScrollbarX() {
            return super.getScrollbarX() + 15;
        }
    }

    private static class CommandShortcutEntry extends ElementListWidget.Entry<CommandShortcutEntry> {
        private final ButtonWidget executeButton;
        private final ButtonWidget deleteButton;

        public CommandShortcutEntry(CommandShortcut shortcut, CommandShortcutScreen commandShortcutScreen) {

            executeButton = ButtonWidget.builder(Text.of(shortcut.name), button -> {
                if (shortcut.execute()) {
                    MinecraftClient.getInstance().setScreen(null);
                }
            }).tooltip(Tooltip.of(Text.of("Press button to run the command"))).dimensions(0, 0, 150, 20).build();

            deleteButton = ButtonWidget.builder(Text.of("Delete"), button -> {
                FrameworkClient.getInstance().getCommandShortcutsManager().getCommandShortcutConfig().delete(shortcut);
                FrameworkClient.getInstance().getCommandShortcutsManager().save();
                MinecraftClient.getInstance().setScreen(null);
            }).tooltip(Tooltip.of(Text.of("Press button to delete shortcut"))).dimensions(0, 0, 50, 20).build();
            if (shortcut.command.equals("@new")) {
                deleteButton.active = false;
                deleteButton.setTooltip(null);
            }
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(executeButton, deleteButton);
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of(executeButton, deleteButton);
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            executeButton.setX(x);
            executeButton.setY(y);
            executeButton.render(context, mouseX, mouseY, tickDelta);

            deleteButton.setX(x + 155);
            deleteButton.setY(y);
            deleteButton.render(context, mouseX, mouseY, tickDelta);
        }
    }


}