package de.toxicfox.framework.client;

import com.mojang.blaze3d.platform.InputConstants;
import de.toxicfox.framework.client.event.EventManager;
import de.toxicfox.framework.client.event.EventTarget;
import de.toxicfox.framework.client.event.impl.TickEvent;
import de.toxicfox.framework.client.registry.FrameworkRegistries;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class FrameworkKeyBinding extends KeyMapping {
    public final static Category MODS = Category.register(Identifier.parse("framework-mods"));
    public final static Category MISC = Category.register(Identifier.parse("framework-misc"));
    public final static Category COMMANDS = Category.register(Identifier.parse("framework-commands"));
    private Lambda lambda;
    private boolean alreadyPressed = false;

    public FrameworkKeyBinding(String translationKey, int code, Category category, Lambda lambda) {
        super(translationKey, code, category);
        this.lambda = lambda;
        init();
    }

    public FrameworkKeyBinding(String translationKey, InputConstants.Type type, int code, Category category, Lambda lambda) {
        super(translationKey, type, code, category);
        this.lambda = lambda;
        init();
    }


    @EventTarget
    public void onTick(TickEvent event) {
        if (isDown()) {
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
        FrameworkRegistries.KEY_BINDINGS.register(getName(), this);
        EventManager.register(this);

        FrameworkClient.LOGGER.info("Registered keybinding {}", getName());
    }

    public void setLambda(Lambda lambda) {
        this.lambda = lambda;
    }

    public interface Lambda {
        void onclick();
    }
}
