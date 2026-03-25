package de.toxicfox.framework.client.screen;

import com.google.common.collect.ImmutableList;
import de.toxicfox.framework.client.FrameworkClient;
import de.toxicfox.framework.client.commandshortcuts.CommandShortcut;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class CommandShortcutScreen extends Screen {


    public CommandShortcutScreen() {
        super(Component.nullToEmpty("Command shortcuts"));

    }

    public static void open() {
        Minecraft.getInstance().setScreen(new CommandShortcutScreen());
    }

    @Override
    protected void init() {
        addRenderableWidget(new CommandShortcutWidget(Minecraft.getInstance(), width, height, 0, 20, FrameworkClient.getInstance().getCommandShortcutsManager().getCommandShortcutConfig().shortcuts, this));
    }

    private static class CommandShortcutWidget extends ContainerObjectSelectionList<CommandShortcutEntry> {
        public CommandShortcutWidget(Minecraft minecraftClient, int width, int height, int y, int entryHeight, CommandShortcut[] shortcuts, CommandShortcutScreen commandShortcutScreen) {
            super(minecraftClient, width, height, y, entryHeight);

            for (CommandShortcut shortcut : shortcuts) {
                addEntry(new CommandShortcutEntry(shortcut, commandShortcutScreen));
            }
        }

        @Override
        protected int scrollBarX() {
            return super.scrollBarX() + 15;
        }
    }

    private static class CommandShortcutEntry extends ContainerObjectSelectionList.Entry<CommandShortcutEntry> {
        private final Button executeButton;
        private final Button deleteButton;

        public CommandShortcutEntry(CommandShortcut shortcut, CommandShortcutScreen commandShortcutScreen) {

            executeButton = Button.builder(Component.nullToEmpty(shortcut.name), button -> {
                if (shortcut.execute()) {
                    Minecraft.getInstance().setScreen(null);
                }
            }).tooltip(Tooltip.create(Component.nullToEmpty("Press button to run the command"))).bounds(0, 0, 150, 20).build();

            deleteButton = Button.builder(Component.nullToEmpty("Delete"), button -> {
                FrameworkClient.getInstance().getCommandShortcutsManager().getCommandShortcutConfig().delete(shortcut);
                FrameworkClient.getInstance().getCommandShortcutsManager().save();
                Minecraft.getInstance().setScreen(null);
            }).tooltip(Tooltip.create(Component.nullToEmpty("Press button to delete shortcut"))).bounds(0, 0, 50, 20).build();
            if (shortcut.command.equals("@new")) {
                deleteButton.active = false;
                deleteButton.setTooltip(null);
            }
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(executeButton, deleteButton);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return ImmutableList.of(executeButton, deleteButton);
        }

        @Override
        public void renderContent(GuiGraphics context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            executeButton.setX(this.getContentX());
            executeButton.setY(this.getContentY());
            executeButton.render(context, mouseX, mouseY, deltaTicks);

            deleteButton.setX(this.getContentX() + 155);
            deleteButton.setY(this.getContentY());
            deleteButton.render(context, mouseX, mouseY, deltaTicks);
        }
    }


}