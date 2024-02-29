package de.glowman554.framework.client;

import de.glowman554.framework.client.event.EventManager;
import de.glowman554.framework.client.event.EventTarget;
import de.glowman554.framework.client.event.impl.TickEvent;
import de.glowman554.framework.client.registry.FrameworkRegistries;
import de.glowman554.framework.mixin.KeyBindingAccessor;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.util.Map;
import java.util.Optional;

public class FrameworkKeyBinding extends KeyBinding {
    public final static String MODS = "key.framework.mods";
    public final static String MISC = "key.framework.misc";
    private Lambda lambda;
    private boolean alreadyPressed = false;

    public FrameworkKeyBinding(String translationKey, int code, String category, Lambda lambda) {
        super(translationKey, code, category);
        this.lambda = lambda;
        init();
    }

    public FrameworkKeyBinding(String translationKey, InputUtil.Type type, int code, String category, Lambda lambda) {
        super(translationKey, type, code, category);
        this.lambda = lambda;
        init();
    }


    @EventTarget
    public void onTick(TickEvent event) {
        if (isPressed()) {
            if (alreadyPressed) {
                return;
            }
            lambda.onclick();
            alreadyPressed = true;
        } else {
            alreadyPressed = false;
        }
    }

    private void init() {
        FrameworkRegistries.KEY_BINDINGS.register(getTranslationKey(), this);
        EventManager.register(this);

        Map<String, Integer> map = KeyBindingAccessor.get_CATEGORY_ORDER_MAP();
        if (!map.containsKey(getCategory())) {
            FrameworkClient.LOGGER.info("Adding new keybinding category {}", getCategory());
            Optional<Integer> largest = map.values().stream().max(Integer::compareTo);
            int largestInt = largest.orElse(0);
            map.put(getCategory(), largestInt + 1);
        }
    }

    public void setLambda(Lambda lambda) {
        this.lambda = lambda;
    }

    public interface Lambda {
        void onclick();
    }
}
