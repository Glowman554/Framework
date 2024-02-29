package de.glowman554.framework.client.hud;

import com.google.common.collect.Sets;
import de.glowman554.framework.client.event.EventManager;
import de.glowman554.framework.client.event.EventTarget;
import de.glowman554.framework.client.event.impl.RenderEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class HUDManager {
    private static HUDManager instance = null;
    private final Set<Renderer> registeredRenderers = Sets.newHashSet();
    private final MinecraftClient mc = MinecraftClient.getInstance();

    private HUDManager() {

    }

    public static HUDManager getInstance() {

        if (instance != null) {
            return instance;
        }

        instance = new HUDManager();
        EventManager.register(instance);
        return instance;
    }

    public void register(Renderer... renderers) {
        Collections.addAll(this.registeredRenderers, renderers);
    }

    public void unregister(Renderer... renderers) {
        for (Renderer renderer : renderers) {
            this.registeredRenderers.remove(renderer);
        }
    }

    public Collection<Renderer> getRegisteredRenderers() {
        return Sets.newHashSet(registeredRenderers);
    }

    @EventTarget
    public void onRender(RenderEvent e) {
        if (mc.currentScreen == null || mc.currentScreen instanceof GenericContainerScreen || mc.currentScreen instanceof ChatScreen) {
            for (Renderer renderer : registeredRenderers) {
                callRenderer(e.getDrawContext(), renderer);
            }
        }
    }

    private void callRenderer(DrawContext drawContext, Renderer renderer) {
        if (!renderer.isEnabled()) {
            return;
        }

        ScreenPosition pos = renderer.getPos();

        if (pos == null) {
            pos = ScreenPosition.fromRelativePosition(0.5, 0.5);
            renderer.setPos(pos);
        }

        renderer.render(drawContext, pos);
    }
}
