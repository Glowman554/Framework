package de.toxicfox.framework.client.screen;

import de.toxicfox.framework.client.FrameworkClient;
import de.toxicfox.framework.client.commandshortcuts.CommandShortcut;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class NewCommandShortcutScreen extends Screen {
    private static final Component ENTER_NAME_TEXT = Component.nullToEmpty("Name");
    private static final Component ENTER_COMMAND_TEXT = Component.nullToEmpty("Command");
    private EditBox nameInput;
    private EditBox commandInput;

    protected NewCommandShortcutScreen() {
        super(Component.nullToEmpty("New command shortcut"));
    }

    public static void open() {
        Minecraft.getInstance().setScreen(new NewCommandShortcutScreen());
    }

    @Override
    protected void init() {
        super.init();

        addRenderableWidget(Button.builder(Component.nullToEmpty("Done"), button -> {
            if (nameInput.getValue().isEmpty() || commandInput.getValue().isEmpty()) {
                return;
            }
            FrameworkClient.getInstance().getCommandShortcutsManager().getCommandShortcutConfig().addNew(new CommandShortcut(nameInput.getValue(), commandInput.getValue()));
            FrameworkClient.getInstance().getCommandShortcutsManager().save();

            Minecraft.getInstance().setScreen(null);
        }).bounds(width / 2 - 100, height - 25, 200, 20).build());
        nameInput = addRenderableWidget(new EditBox(this.font, this.width / 2 - 100, 66, 200, 20, Component.nullToEmpty("")));
        commandInput = addRenderableWidget(new EditBox(this.font, this.width / 2 - 100, 106, 200, 20, Component.nullToEmpty("")));
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        context.drawString(this.font, ENTER_NAME_TEXT, this.width / 2 - 100 + 1, 53, 10526880);
        context.drawString(this.font, ENTER_COMMAND_TEXT, this.width / 2 - 100 + 1, 94, 10526880);
    }

}