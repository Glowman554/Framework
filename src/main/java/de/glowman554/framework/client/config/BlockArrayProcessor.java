package de.glowman554.framework.client.config;

import de.glowman554.config.auto.JsonProcessor;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.shadew.json.JsonNode;

import java.util.ArrayList;

public class BlockArrayProcessor implements JsonProcessor {
    @Override
    public JsonNode toJson(Object o) {
        ArrayList<String> blockIds = new ArrayList<>();
        Block[] blocks = (Block[]) o;
        for (Block b : blocks) {
            blockIds.add(Registries.BLOCK.getId(b).toString());
        }
        return JsonNode.stringArray(blockIds);
    }

    @Override
    public Object fromJson(JsonNode jsonNode, Object o) {
        if (jsonNode == null) {
            return o;
        }
        String[] blockIds = jsonNode.asStringArray();
        ArrayList<Block> blocks = new ArrayList<>();
        for (String blockId : blockIds) {
            blocks.add(Registries.BLOCK.get(new Identifier(blockId)));
        }
        return blocks.toArray(Block[]::new);
    }
}