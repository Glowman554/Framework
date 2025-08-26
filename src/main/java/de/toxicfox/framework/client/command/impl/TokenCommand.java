package de.toxicfox.framework.client.command.impl;

import de.toxicfox.framework.client.command.Command;
import de.toxicfox.framework.client.command.CommandEvent;
import net.minecraft.client.MinecraftClient;

public class TokenCommand extends Command {
    public TokenCommand() {
        super("Show your authentication token");
    }

    @Override
    public void execute(CommandEvent event) {
        String token = MinecraftClient.getInstance().getSession().getAccessToken();
        event.commandSuccess(token);
        copyToClipboard(token);
    }

    private void copyToClipboard(String string) {
        MinecraftClient.getInstance().keyboard.setClipboard(string);
    }
}
