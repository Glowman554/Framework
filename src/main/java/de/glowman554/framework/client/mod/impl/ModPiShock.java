package de.glowman554.framework.client.mod.impl;

import de.toxicfox.config.auto.Saved;
import de.glowman554.framework.client.FrameworkClient;
import de.glowman554.framework.client.command.impl.ShockCommand;
import de.glowman554.framework.client.config.Configurable;
import de.glowman554.framework.client.event.EventTarget;
import de.glowman554.framework.client.event.impl.DeathEvent;
import de.glowman554.framework.client.mod.Mod;
import de.glowman554.framework.client.screen.TestButtonExecutor;
import de.glowman554.framework.client.telemetry.buildin.TelemetryModCollector;
import de.glowman554.framework.client.utils.pishock.PiShockClient;

@TelemetryModCollector.Disabled
public class ModPiShock extends Mod {
    @Saved
    @Configurable(text = "Shock duration")
    private int duration = 1;
    @Saved
    @Configurable(text = "Shock intensity")
    private int intensity = 50;

    @Saved
    @Configurable(text = "PiShock username")
    private String username = "";
    @Saved
    @Configurable(text = "PiShock api key")
    private String apikey = "";
    @Saved
    @Configurable(text = "Application name")
    private String name = "Framework";

    @Configurable(text = "Test connection")
    private TestButtonExecutor testButton = this::triggerTest;

    private PiShockClient client;

    @Override
    public void setEnabled(boolean newEnabled) {
        super.setEnabled(newEnabled);

        if (isEnabled()) {
            client = new PiShockClient(username, apikey);
            FrameworkClient.getInstance().getCommandManager().addCommand("pishock-shock", new ShockCommand(this::trigger));
        } else {
            FrameworkClient.getInstance().getCommandManager().removeCommand("pishock-shock");

            if (client != null) {
                try {
                    client.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                client = null;
            }
        }
    }

    @EventTarget
    public void onDeath(DeathEvent event) {
        new Thread(this::trigger).start();
    }

    private void triggerTest() {
        try (PiShockClient tmpClient = new PiShockClient(username, apikey)) {
            tmpClient.trigger(intensity, duration * 1000, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void trigger() {
        client.trigger(intensity, duration * 1000, name);
    }

    @Override
    public String getId() {
        return "pi-shock";
    }

    @Override
    public String getName() {
        return "PiShock";
    }

    @Override
    public boolean isHacked() {
        return false;
    }
}
