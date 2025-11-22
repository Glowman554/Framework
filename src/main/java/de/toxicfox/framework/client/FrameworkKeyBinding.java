package de.toxicfox.framework.client;

import de.toxicfox.framework.client.event.EventManager;
import de.toxicfox.framework.client.event.EventTarget;
import de.toxicfox.framework.client.event.impl.TickEvent;
import de.toxicfox.framework.client.registry.FrameworkRegistries;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

public class FrameworkKeyBinding extends KeyBinding {
    public final static Category MODS = Category.create(Identifier.of("framework-mods"));
    public final static Category MISC = Category.create(Identifier.of("framework-misc"));
    public final static Category COMMANDS = Category.create(Identifier.of("framework-commands"));
    private Lambda lambda;
    private boolean alreadyPressed = false;

    public FrameworkKeyBinding(String translationKey, int code, Category category, Lambda lambda) {
        super(translationKey, code, category);
        this.lambda = lambda;
        init();
    }

    public FrameworkKeyBinding(String translationKey, InputUtil.Type type, int code, Category category, Lambda lambda) {
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
        FrameworkRegistries.KEY_BINDINGS.register(getId(), this);
        EventManager.register(this);

        FrameworkClient.LOGGER.info("Registered keybinding {}", getId());
    }

    public void setLambda(Lambda lambda) {
        this.lambda = lambda;
    }

    public interface Lambda {
        void onclick();
    }
}
