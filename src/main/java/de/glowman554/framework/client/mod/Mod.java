package de.glowman554.framework.client.mod;

import de.glowman554.config.ConfigManager;
import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;
import de.glowman554.framework.client.FrameworkClient;
import de.glowman554.framework.client.event.EventManager;
import de.glowman554.framework.client.registry.FrameworkRegistries;
import de.glowman554.framework.client.telemetry.buildin.TelemetryModCollector;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;

public abstract class Mod extends AutoSavable {
    protected MinecraftClient mc;
    protected TextRenderer textRenderer;
    private ConfigManager configManager;

    @Saved
    private boolean enabled;


    public void configure(ConfigManager configManager) {
        this.mc = MinecraftClient.getInstance();
        this.textRenderer = mc.textRenderer;
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
            EventManager.register(this);
        } else {
            EventManager.unregister(this);
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
        SystemToast.show(mc.getToastManager(), SystemToast.Type.PERIODIC_NOTIFICATION, Text.of(getName()), Text.of(String.format("%s is now %s", getName(), isEnabled() ? "enabled" : "disabled")));
    }

    public abstract String getId();

    public abstract String getName();

    public boolean defaultEnable() {
        return false;
    }

    public abstract boolean isHacked();
}
