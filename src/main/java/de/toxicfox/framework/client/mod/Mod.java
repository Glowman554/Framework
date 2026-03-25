package de.toxicfox.framework.client.mod;

import de.toxicfox.config.ConfigManager;
import de.toxicfox.config.auto.AutoSavable;
import de.toxicfox.config.auto.Saved;
import de.toxicfox.framework.client.FrameworkClient;
import de.toxicfox.framework.client.event.EventManager;
import de.toxicfox.framework.client.registry.FrameworkRegistries;
import de.toxicfox.framework.client.telemetry.buildin.TelemetryModCollector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;

public abstract class Mod extends AutoSavable {
    protected Minecraft mc;
    protected Font textRenderer;
    private ConfigManager configManager;

    private boolean eventManagerRegistered = false;

    @Saved
    private boolean enabled;


    public void configure(ConfigManager configManager) {
        this.mc = Minecraft.getInstance();
        this.textRenderer = mc.font;
        this.configManager = configManager;

        try {
            configManager.loadValue(getId(), this);
        } catch (IllegalArgumentException e) {
            enabled = defaultEnable();
        }

        setEnabled(enabled);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean newEnabled) {
        this.enabled = newEnabled;

        if (!FrameworkClient.getInstance().getConfig().enableHacks && isHacked()) {
            enabled = false;
        }

        save();

        if (enabled) {
            if (!eventManagerRegistered) {
                EventManager.register(this);
                eventManagerRegistered = true;
            }
        } else {
            EventManager.unregister(this);
            eventManagerRegistered = false;
        }

        try {
            TelemetryModCollector collector = (TelemetryModCollector) FrameworkRegistries.TELEMETRY_COLLECTORS.get(TelemetryModCollector.class);
            collector.send(this);
        } catch (IllegalArgumentException ignored) {

        }
    }


    public void save() {
        configManager.setValue(getId(), this);
    }

    public void onKeybinding() {
        setEnabled(!isEnabled());
        SystemToast.addOrUpdate(mc.getToastManager(), SystemToast.SystemToastId.PERIODIC_NOTIFICATION, Component.nullToEmpty(getName()), Component.nullToEmpty(String.format("%s is now %s", getName(), isEnabled() ? "enabled" : "disabled")));
    }

    public abstract String getId();

    public abstract String getName();

    public boolean defaultEnable() {
        return false;
    }

    public abstract boolean isHacked();

    public Minecraft getMc() {
        return mc;
    }
}
