package de.toxicfox.framework.client.config;

import de.toxicfox.config.auto.JsonProcessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.shadew.json.JsonNode;

import java.util.ArrayList;

public class BlockArrayProcessor implements JsonProcessor {
    @Override
    public JsonNode toJson(Object o) {
        ArrayList<String> blockIds = new ArrayList<>();
        Block[] blocks = (Block[]) o;
        for (Block b : blocks) {
            blockIds.add(BuiltInRegistries.BLOCK.getKey(b).toString());
        }
        return JsonNode.stringArray(blockIds);
    }

    @Override
    public Object fromJson(JsonNode jsonNode, Object o, boolean optional) {
        if (jsonNode == null) {
            if (optional) {
                return o;
            }
            throw new RuntimeException("Missing field");
        }
        String[] blockIds = jsonNode.asStringArray();
        ArrayList<Block> blocks = new ArrayList<>();
        for (String blockId : blockIds) {
            blocks.add(BuiltInRegistries.BLOCK.getValue(Identifier.parse(blockId)));
        }
        return blocks.toArray(Block[]::new);
    }
}