package de.glowman554.framework.client.mod.impl;

import de.glowman554.config.auto.Saved;
import de.glowman554.framework.client.FrameworkClient;
import de.glowman554.framework.client.command.Command;
import de.glowman554.framework.client.command.CommandEvent;
import de.glowman554.framework.client.mod.Mod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModHidePlayers extends Mod {
    @Saved
    private String[] hiddenPlayers = new String[]{};

    @Override
    public String getId() {
        return "hide-players";
    }

    @Override
    public String getName() {
        return "Hide players";
    }

    @Override
    public void setEnabled(boolean newEnabled) {
        super.setEnabled(newEnabled);

        if (isEnabled()) {
            FrameworkClient.getInstance().getCommandManager().addCommand("hidden", new HiddenCommand(this));
        } else {
            FrameworkClient.getInstance().getCommandManager().removeCommand("hidden");
        }
    }

    public boolean isHidden(String uuid) {
        if (isEnabled()) {
            for (String hidden : hiddenPlayers) {
                if (hidden.equals(uuid)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean isHacked() {
        return false;
    }

    private class HiddenCommand extends Command {
        private final ModHidePlayers mod;

        public HiddenCommand(ModHidePlayers mod) {
            super("Hide / unhide players!");
            this.mod = mod;
        }

        @Override
        public void execute(CommandEvent event) {
            if (event.args().length != 2) {
                event.commandFail("hidden <hide/unhide> <uuid>");
            } else {
                List<String> list = new ArrayList<>(Arrays.asList(mod.hiddenPlayers));

                switch (event.args()[0]) {
                    case "hide":
                        list.add(event.args()[1]);
                        break;

                    case "unhide":
                        list.remove(event.args()[1]);
                        break;

                    default:
                        event.commandFail("Unknown sub-command " + event.args()[0]);
                        break;
                }

                mod.hiddenPlayers = list.toArray(String[]::new);
                mod.save();
            }
        }
    }
}
