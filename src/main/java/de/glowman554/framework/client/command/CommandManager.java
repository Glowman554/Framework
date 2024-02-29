package de.glowman554.framework.client.command;

import de.glowman554.framework.client.FrameworkClient;

import java.util.HashMap;

public class CommandManager {
    public final String prefix;
    private final HashMap<String, Command> commands;

    public CommandManager(String prefix) {
        this.prefix = prefix;
        commands = new HashMap<String, Command>();
    }

    public void onCommand(CommandEvent event) throws Exception {
        if (!event.command().startsWith(prefix)) {
            return;
        }

        if (event.command().equals(prefix + "help")) {
            if (event.args().length == 0) {
                StringBuilder builder = new StringBuilder();
                builder.append("§f--§aHelp§f--\n");

                commands.forEach((key, value) -> {
                    builder.append("§1").append(key).append("§f -> ").append(value.getShortHelp()).append("\n");
                });

                event.commandSuccess(builder.toString());
            } else {
                event.commandFail("Too many arguments.");
            }
        } else {
            Command command = commands.get(event.command());

            if (command != null) {
                command.execute(event);
            } else {
                event.commandFail("Command not found.");
            }
        }
    }


    public void addCommand(String what, Command command) {
        what = prefix + what;

        command.register();

        commands.put(what, command);
        FrameworkClient.LOGGER.info("[{}] Command register complete", what);
    }

    public void removeCommand(String what) {
        what = prefix + what;

        if (commands.containsKey(what)) {
            commands.remove(what);
            FrameworkClient.LOGGER.info("[{}] Command removed", what);
        }
    }
}