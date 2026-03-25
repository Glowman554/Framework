package de.toxicfox.framework.client.mod.impl;

import de.toxicfox.config.auto.Saved;
import de.toxicfox.framework.client.config.Configurable;
import de.toxicfox.framework.client.event.EventTarget;
import de.toxicfox.framework.client.event.impl.ClientPlayerTickEvent;
import de.toxicfox.framework.client.mod.Mod;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;

public class ModAutoEat extends Mod {
    @Saved
    @Configurable(text = "Eat at hunger level")
    private final int eatAt = 15;
    private int oldSlot = -1;

    @Override
    public String getId() {
        return "auto-eat";
    }

    @Override
    public String getName() {
        return "Auto eat";
    }

    @Override
    public void setEnabled(boolean newEnabled) {
        super.setEnabled(newEnabled);
        stopEating();
    }

    @EventTarget
    public void onClientPlayerTickEvent(ClientPlayerTickEvent event) {
        LocalPlayer player = mc.player;

        if (!shouldEat()) {
            stopEating();
            return;
        }

        if (isEating()) {
            assert mc.player != null;
            if (mc.player.getInventory().getSelectedItem() == null || mc.player.getInventory().getSelectedItem().get(DataComponents.FOOD) == null) {
                stopEating();
                return;
            }
        }

        assert player != null;
        FoodData hungerManager = player.getFoodData();
        int foodLevel = hungerManager.getFoodLevel();


        if (foodLevel < eatAt) {
            eat();
        }
    }

    private void eat() {
        assert mc.player != null;
        Inventory inventory = mc.player.getInventory();
        int foodSlot = findBestFoodSlot();

        if (foodSlot == -1) {
            stopEating();
            return;
        }

        if (foodSlot < 9) {
            if (!isEating()) {
                oldSlot = inventory.getSelectedSlot();
            }

            inventory.setSelectedSlot(foodSlot);
        }

        mc.options.keyUse.setDown(true);
        assert mc.gameMode != null;
        mc.gameMode.useItem(mc.player, InteractionHand.MAIN_HAND);
    }

    private int findBestFoodSlot() {
        assert mc.player != null;
        Inventory inventory = mc.player.getInventory();
        FoodProperties bestFood = null;
        int bestSlot = -1;
        for (int slot = 0; slot < 9; slot++) {
            ItemStack item = inventory.getItem(slot);
            FoodProperties foodComponent = item.get(DataComponents.FOOD);
            if (foodComponent != null) {

                if (foodComponent == Foods.CHORUS_FRUIT) {
                    continue;
                }

                if (bestFood == null || bestFood.saturation() < foodComponent.saturation()) {
                    bestFood = foodComponent;
                    bestSlot = slot;
                }
            }
        }

        return bestSlot;
    }

    private boolean shouldEat() {
        assert mc.player != null;
        return !mc.player.getAbilities().instabuild && mc.player.canEat(false) && !(mc.hitResult instanceof EntityHitResult);
    }

    private void stopEating() {
        if (isEating()) {
            mc.options.keyUse.setDown(false);
            assert mc.player != null;
            mc.player.getInventory().setSelectedSlot(oldSlot);
            oldSlot = -1;
        }
    }

    public boolean isEating() {
        return oldSlot != -1;
    }

    @Override
    public boolean isHacked() {
        return true;
    }
}
