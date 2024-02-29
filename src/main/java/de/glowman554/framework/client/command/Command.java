package de.glowman554.framework.client.command;

public abstract class Command {
    private final String shortHelp;

    protected Command(String shortHelp) {
        this.shortHelp = shortHelp;
    }

    public abstract void execute(CommandEvent event);

    public void register() {
    }

    public String getShortHelp() {
        return shortHelp;
    }
}
