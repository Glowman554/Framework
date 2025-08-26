package de.toxicfox.framework.client.command.impl;

import de.toxicfox.framework.client.command.Command;
import de.toxicfox.framework.client.command.CommandEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.EntityHitResult;

public class UuidCommand extends Command {
    public UuidCommand() {
        super("Get uuid of player you are targeting!");
    }

    @Override
    public void execute(CommandEvent event) {
        if (event.args().length != 0) {
            event.commandFail("Command takes no arguments!");
        } else {
            if (MinecraftClient.getInstance().crosshairTarget instanceof EntityHitResult entityHitResult) {
                event.commandSuccess(entityHitResult.getEntity().getUuidAsString());
                copyToClipboard(entityHitResult.getEntity().getUuidAsString());
            } else {
                event.commandFail("Not targeting an entity!");
            }
        }
    }

    private void copyToClipboard(String string) {
        MinecraftClient.getInstance().keyboard.setClipboard(string);
    }
}
