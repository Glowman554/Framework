package de.toxicfox.framework.client.mod.impl;

import de.toxicfox.config.ConfigManager;
import de.toxicfox.config.Savable;
import de.toxicfox.config.auto.AutoSavable;
import de.toxicfox.config.auto.Saved;
import de.toxicfox.config.auto.processors.SavableArrayProcessor;
import de.toxicfox.framework.client.event.EventTarget;
import de.toxicfox.framework.client.event.impl.DeathEvent;
import de.toxicfox.framework.client.hud.ScreenPosition;
import de.toxicfox.framework.client.mod.ModDraggable;
import de.toxicfox.framework.mixin.MinecraftServerAccessor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.util.math.BlockPos;
import net.shadew.json.JsonNode;

import java.util.Objects;

public class ModDeathPositionSaver extends ModDraggable {
    private final DeathPositionSave lastDeaths = new DeathPositionSave();

    public ModDeathPositionSaver() {
        pos = new ScreenPosition(0.4, 0);
    }

    @Override
    public int getWidth() {
        return textRenderer.getWidth("New World, minecraft:overworld");
    }

    @Override
    public int getHeight() {
        return textRenderer.fontHeight * 2;
    }

    @Override
    public void render(DrawContext drawContext, ScreenPosition pos) {
        if (lastDeaths.lastDeaths.length > 0) {
            DeathPosition position = lastDeaths.lastDeaths[lastDeaths.lastDeaths.length - 1];
            drawContext.drawText(textRenderer, String.format("Death XYZ: %d, %d, %d", position.x, position.y, position.z), pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, -1, true);
            drawContext.drawText(textRenderer, String.format(position.address + ", " + position.dimension), pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1 + textRenderer.fontHeight, -1, true);
        }
    }

    @Override
    public void renderDummy(DrawContext drawContext, ScreenPosition pos) {
        drawContext.drawText(textRenderer, "Death XYZ: 0, 0, 0", pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, -1, true);
        drawContext.drawText(textRenderer, "New World, minecraft:overworld", pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1 + textRenderer.fontHeight, -1, true);
    }

    @Override
    public String getId() {
        return "death-position-saver";
    }

    @Override
    public String getName() {
        return "Death position saver";
    }

    @EventTarget
    public void onDeathEvent(DeathEvent event) {
        assert mc.player != null;
        assert mc.world != null;
        ServerInfo info = Objects.requireNonNull(mc.getNetworkHandler()).getServerInfo();
        if (info != null) {
            lastDeaths.add(mc.player.getBlockPos(), mc.world.getRegistryKey().getValue().toString(), info.address);
        } else {
            MinecraftServerAccessor server = (MinecraftServerAccessor) mc.getServer();
            assert server != null;
            lastDeaths.add(mc.player.getBlockPos(), mc.world.getRegistryKey().getValue().toString(), server.getSession().getDirectoryName());
        }
    }

    @Override
    public boolean isHacked() {
        return false;
    }

    private static class DeathPositionSave implements Savable {
        private final ConfigManager deathConfigManager = new ConfigManager("deaths", false);
        private DeathPosition[] lastDeaths = new DeathPosition[]{};

        private DeathPositionSave() {
            try {
                load();
            } catch (IllegalArgumentException ignored) {
            }
        }

        private void load() {
            deathConfigManager.loadValue("deaths", this);
        }

        private void save() {
            deathConfigManager.setValue("deaths", this);
        }

        public void add(BlockPos blockPos, String dimension, String address) {
            DeathPosition[] old = lastDeaths;
            lastDeaths = new DeathPosition[old.length + 1];
            System.arraycopy(old, 0, lastDeaths, 0, old.length);
            lastDeaths[old.length] = new DeathPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ(), dimension, address);
            save();
        }

        @Override
        public void fromJSON(JsonNode jsonNode) {
            lastDeaths = (DeathPosition[]) new SavableArrayProcessor(DeathPosition::new, DeathPosition[]::new).fromJson(jsonNode, new DeathPosition[]{}, false);
        }

        @Override
        public JsonNode toJSON() {
            return new SavableArrayProcessor(DeathPosition::new, DeathPosition[]::new).toJson(lastDeaths);
        }
    }

    private static class DeathPosition extends AutoSavable {
        @Saved
        private int x;
        @Saved
        private int y;
        @Saved
        private int z;
        @Saved
        private String dimension = "";
        @Saved
        private String address = "";

        public DeathPosition() {

        }

        public DeathPosition(int x, int y, int z, String dimension, String address) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.dimension = dimension;
            this.address = address;
        }
    }
}
