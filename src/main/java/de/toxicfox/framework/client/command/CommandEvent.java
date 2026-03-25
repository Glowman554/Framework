package de.toxicfox.framework.client.command;

import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public record CommandEvent(String message, String command, String[] args) {

    public static String[] getArguments(String[] array) {
        String[] args = new String[array.length - 1];
        System.arraycopy(array, 1, args, 0, array.length - 1);
        return args;
    }

    public static void sendText(String text) {
        Minecraft.getInstance().gui.getChat().addMessage(Component.nullToEmpty(text), null, GuiMessageTag.chatNotSecure());
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
