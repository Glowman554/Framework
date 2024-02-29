package de.glowman554.framework.client.screen;

import de.glowman554.framework.client.FrameworkClient;
import de.glowman554.framework.client.commandshortcuts.CommandShortcut;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class NewCommandShortcutScreen extends Screen {
    private static final Text ENTER_NAME_TEXT = Text.of("Name");
    private static final Text ENTER_COMMAND_TEXT = Text.of("Command");
    private TextFieldWidget nameInput;
    private TextFieldWidget commandInput;

    protected NewCommandShortcutScreen() {
        super(Text.of("New command shortcut"));
    }

    public static void open() {
        MinecraftClient.getInstance().setScreen(new NewCommandShortcutScreen());
    }

    @Override
    protected void init() {
        super.init();

        addDrawableChild(ButtonWidget.builder(Text.of("Done"), button -> {
            if (nameInput.getText().isEmpty() || commandInput.getText().isEmpty()) {
                return;
            }
            FrameworkClient.getInstance().getCommandShortcutsManager().getCommandShortcutConfig().addNew(new CommandShortcut(nameInput.getText(), commandInput.getText()));
            FrameworkClient.getInstance().getCommandShortcutsManager().save();

            MinecraftClient.getInstance().setScreen(null);
        }).dimensions(width / 2 - 100, height - 25, 200, 20).build());
        nameInput = addDrawableChild(new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 66, 200, 20, Text.of("")));
        commandInput = addDrawableChild(new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 106, 200, 20, Text.of("")));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        context.drawTextWithShadow(this.textRenderer, ENTER_NAME_TEXT, this.width / 2 - 100 + 1, 53, 10526880);
        context.drawTextWithShadow(this.textRenderer, ENTER_COMMAND_TEXT, this.width / 2 - 100 + 1, 94, 10526880);
    }

}