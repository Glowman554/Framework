package de.toxicfox.framework.client.config;

import de.toxicfox.config.auto.AutoSavable;
import net.minecraft.world.level.block.Block;

public class Processors {
    public static void register() {
        AutoSavable.register(Block[].class, new BlockArrayProcessor());
    }
}
