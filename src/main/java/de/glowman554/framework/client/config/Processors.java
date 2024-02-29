package de.glowman554.framework.client.config;

import de.glowman554.config.auto.AutoSavable;
import net.minecraft.block.Block;

public class Processors {
    public static void register() {
        AutoSavable.register(Block[].class, new BlockArrayProcessor());
    }
}
