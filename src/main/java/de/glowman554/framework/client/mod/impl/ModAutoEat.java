package de.glowman554.framework.client.mod.impl;

import de.glowman554.config.auto.Saved;
import de.glowman554.framework.client.config.Configurable;
import de.glowman554.framework.client.event.EventTarget;
import de.glowman554.framework.client.event.impl.ClientPlayerTickEvent;
import de.glowman554.framework.client.mod.Mod;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;

public class ModAutoEat extends Mod {
    private int oldSlot = -1;

    @Saved
    @Configurable(text = "Eat at hunger level")
    private final int eatAt = 15;


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
        ClientPlayerEntity player = mc.player;

        if (!shouldEat()) {
            stopEating();
            return;
        }

        if (isEating()) {
            assert mc.player != null;
            if (mc.player.getInventory().getMainHandStack() == null || mc.player.getInventory().getMainHandStack().get(DataComponentTypes.FOOD) == null) {
                stopEating();
                return;
            }
        }

        assert player != null;
        HungerManager hungerManager = player.getHungerManager();
        int foodLevel = hungerManager.getFoodLevel();


        if (foodLevel < eatAt) {
            eat();
        }
    }

    private void eat() {
        assert mc.player != null;
        PlayerInventory inventory = mc.player.getInventory();
        int foodSlot = findBestFoodSlot();

        if (foodSlot == -1) {
            stopEating();
            return;
        }

        if (foodSlot < 9) {
            if (!isEating()) {
                oldSlot = inventory.selectedSlot;
            }

            inventory.selectedSlot = foodSlot;
        }

        mc.options.useKey.setPressed(true);
        assert mc.interactionManager != null;
        mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
    }

    private int findBestFoodSlot() {
        assert mc.player != null;
        PlayerInventory inventory = mc.player.getInventory();
        FoodComponent bestFood = null;
        int bestSlot = -1;
        for (int slot = 0; slot < 9; slot++) {
            ItemStack item = inventory.getStack(slot);
            FoodComponent foodComponent = item.get(DataComponentTypes.FOOD);
            if (foodComponent != null) {

                if (foodComponent == FoodComponents.CHORUS_FRUIT) {
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
        return !mc.player.getAbilities().creativeMode && mc.player.canConsume(false) && !(mc.crosshairTarget instanceof EntityHitResult);
    }

    private void stopEating() {
        if (isEating()) {
            mc.options.useKey.setPressed(false);
            assert mc.player != null;
            mc.player.getInventory().selectedSlot = oldSlot;
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
