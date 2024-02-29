package de.glowman554.framework.client.mod.impl;

import de.glowman554.config.auto.Saved;
import de.glowman554.framework.client.mod.Mod;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public class ModXRay extends Mod {
    @Saved
    private final Block[] blocks = new Block[]{Blocks.ANCIENT_DEBRIS, Blocks.ANVIL, Blocks.BEACON, Blocks.BONE_BLOCK, Blocks.BOOKSHELF, Blocks.BREWING_STAND, Blocks.CHAIN_COMMAND_BLOCK, Blocks.CHEST, Blocks.CLAY, Blocks.COAL_BLOCK, Blocks.COAL_ORE, Blocks.COMMAND_BLOCK, Blocks.COPPER_ORE, Blocks.CRAFTER, Blocks.CRAFTING_TABLE, Blocks.DECORATED_POT, Blocks.DEEPSLATE_COAL_ORE, Blocks.DEEPSLATE_COPPER_ORE, Blocks.DEEPSLATE_DIAMOND_ORE, Blocks.DEEPSLATE_EMERALD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.DEEPSLATE_IRON_ORE, Blocks.DEEPSLATE_LAPIS_ORE, Blocks.DEEPSLATE_REDSTONE_ORE, Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE, Blocks.DISPENSER, Blocks.DROPPER, Blocks.EMERALD_BLOCK, Blocks.EMERALD_ORE, Blocks.ENCHANTING_TABLE, Blocks.END_PORTAL, Blocks.END_PORTAL_FRAME, Blocks.ENDER_CHEST, Blocks.FURNACE, Blocks.GLOWSTONE, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.HOPPER, Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LADDER, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE, Blocks.LAVA, Blocks.LODESTONE, Blocks.MOSSY_COBBLESTONE, Blocks.NETHER_GOLD_ORE, Blocks.NETHER_PORTAL, Blocks.NETHER_QUARTZ_ORE, Blocks.RAW_COPPER_BLOCK, Blocks.RAW_GOLD_BLOCK, Blocks.RAW_IRON_BLOCK, Blocks.REDSTONE_BLOCK, Blocks.REDSTONE_ORE, Blocks.REPEATING_COMMAND_BLOCK, Blocks.SPAWNER, Blocks.SUSPICIOUS_GRAVEL, Blocks.SUSPICIOUS_SAND, Blocks.TNT, Blocks.TORCH, Blocks.TRAPPED_CHEST, Blocks.WATER};

    @Override
    public String getId() {
        return "xray";
    }

    @Override
    public String getName() {
        return "X Ray";
    }

    @Override
    public void setEnabled(boolean newEnabled) {
        super.setEnabled(newEnabled);
        mc.worldRenderer.reload();
    }

    public boolean check(Block target) {
        for (Block block : blocks) {
            if (block == target) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isHacked() {
        return true;
    }
}
