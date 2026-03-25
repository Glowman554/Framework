package de.toxicfox.framework.client.hud;

import com.google.common.collect.Sets;
import de.toxicfox.framework.client.event.EventManager;
import de.toxicfox.framework.client.event.EventTarget;
import de.toxicfox.framework.client.event.impl.RenderEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;

public class HUDManager {
    private static HUDManager instance = null;
    private final Set<Renderer> registeredRenderers = Sets.newHashSet();
    private final Minecraft mc = Minecraft.getInstance();

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
        if (mc.screen == null || mc.screen instanceof ContainerScreen || mc.screen instanceof ChatScreen) {
            for (Renderer renderer : registeredRenderers) {
                callRenderer(e.getDrawContext(), renderer);
            }
        }
    }

    private void callRenderer(GuiGraphicsExtractor drawContext, Renderer renderer) {
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
