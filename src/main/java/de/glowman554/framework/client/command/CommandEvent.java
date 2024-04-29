package de.glowman554.framework.client.command;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.text.Text;

public record CommandEvent(String message, String command, String[] args) {

    public static String[] getArguments(String[] array) {
        String[] args = new String[array.length - 1];
        System.arraycopy(array, 1, args, 0, array.length - 1);
        return args;
    }

    public static void sendText(String text) {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of(text), null, MessageIndicator.notSecure());
    }

    public static CommandEvent from(String message) {
        return new CommandEvent(message, message.split(" ")[0], getArguments(message.split(" ")));
    }

    public void commandFail(String reason) {
        sendText("§4" + reason);
    }

    public void commandSuccess(String reason) {
        sendText(reason);
    }
}
