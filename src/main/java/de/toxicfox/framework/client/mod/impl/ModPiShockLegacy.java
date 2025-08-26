package de.toxicfox.framework.client.mod.impl;

import de.toxicfox.config.auto.Saved;
import de.toxicfox.framework.client.FrameworkClient;
import de.toxicfox.framework.client.command.impl.ShockCommand;
import de.toxicfox.framework.client.config.Configurable;
import de.toxicfox.framework.client.event.EventTarget;
import de.toxicfox.framework.client.event.impl.DeathEvent;
import de.toxicfox.framework.client.mod.Mod;
import de.toxicfox.framework.client.screen.TestButtonExecutor;
import de.toxicfox.framework.client.telemetry.buildin.TelemetryModCollector.Disabled;
import de.toxicfox.framework.client.utils.WebClient;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

import java.io.IOException;
import java.util.Map;

@Disabled
public class ModPiShockLegacy extends Mod {
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
    @Configurable(text = "PiShock share-code")
    private String code = "";
    @Saved
    @Configurable(text = "Application name")
    private String name = "Framework";

    @Configurable(text = "Test connection")
    private TestButtonExecutor testButton = this::trigger;

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

    @Override
    public void setEnabled(boolean newEnabled) {
        super.setEnabled(newEnabled);


        if (isEnabled()) {
            FrameworkClient.getInstance().getCommandManager().addCommand("pishock-shock", new ShockCommand(this::trigger));
        } else {
            FrameworkClient.getInstance().getCommandManager().removeCommand("pishock-shock");
        }
    }

    @EventTarget
    public void onDeath(DeathEvent event) {
        new Thread(this::trigger).start();
    }

    private void trigger() {
        JsonNode root = JsonNode.object();
        root.set("Username", username);
        root.set("Name", name);
        root.set("Code", code);
        root.set("Intensity", String.valueOf(intensity));
        root.set("Duration", String.valueOf(duration));
        root.set("Apikey", apikey);
        root.set("Op", String.valueOf(Operation.Shock.op));

        try {
            WebClient.post("https://do.pishock.com/api/apioperate/", Json.json().serialize(root), Map.of("Content-Type", "application/json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static enum Operation {
        Shock(0), Vibrate(1), Beep(2);

        private Operation(int op) {
            this.op = op;
        }

        private final int op;
    }

}
