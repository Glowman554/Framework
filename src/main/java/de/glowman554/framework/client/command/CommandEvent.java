package de.glowman554.framework.client.command;

import de.glowman554.framework.mixin.ChatHudAccessor;
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
        ChatHudAccessor accessor = (ChatHudAccessor) MinecraftClient.getInstance().inGameHud.getChatHud();
        accessor.callAddMessage(Text.of(text), null, MinecraftClient.getInstance().inGameHud.getTicks(), MessageIndicator.notSecure(), false);
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
