package de.toxicfox.framework.client.command.impl;

import de.toxicfox.framework.client.command.Command;
import de.toxicfox.framework.client.command.CommandEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.EntityHitResult;

public class UuidCommand extends Command {
    public UuidCommand() {
        super("Get uuid of player you are targeting!");
    }

    @Override
    public void execute(CommandEvent event) {
        if (event.args().length != 0) {
            event.commandFail("Command takes no arguments!");
        } else {
            if (Minecraft.getInstance().hitResult instanceof EntityHitResult entityHitResult) {
                event.commandSuccess(entityHitResult.getEntity().getStringUUID());
                copyToClipboard(entityHitResult.getEntity().getStringUUID());
            } else {
                event.commandFail("Not targeting an entity!");
            }
        }
    }

    private void copyToClipboard(String string) {
        Minecraft.getInstance().keyboardHandler.setClipboard(string);
    }
}
