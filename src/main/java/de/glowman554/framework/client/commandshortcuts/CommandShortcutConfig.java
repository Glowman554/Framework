package de.glowman554.framework.client.commandshortcuts;

import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.JsonProcessor;
import de.glowman554.config.auto.Processor;
import de.glowman554.config.auto.Saved;
import de.glowman554.config.auto.processors.SavableArrayProcessor;
import net.shadew.json.JsonNode;

import java.util.ArrayList;

public class CommandShortcutConfig extends AutoSavable {
    @Processor(target = CommandShortcut[].class)
    private final JsonProcessor shortcutsProcessor = new SavableArrayProcessor(CommandShortcut::new, CommandShortcut[]::new);
    @Saved
    public CommandShortcut[] shortcuts = new CommandShortcut[]{
            new CommandShortcut("Add new shortcut", "@new"),
            new CommandShortcut("Gamemode creative", "gamemode creative"),
            new CommandShortcut("Gamemode survival", "gamemode survival")
    };

    public void addNew(CommandShortcut shortcut) {
        CommandShortcut[] old = shortcuts;
        shortcuts = new CommandShortcut[old.length + 1];
        System.arraycopy(old, 0, shortcuts, 0, old.length);
        shortcuts[old.length] = shortcut;
    }

    @Override
    public void fromJSON(JsonNode node) {
        super.fromJSON(node);
    }

    @Override
    public JsonNode toJSON() {
        return super.toJSON();
    }

    public void delete(CommandShortcut shortcut) {
        CommandShortcut[] old = shortcuts;
        ArrayList<CommandShortcut> newList = new ArrayList<>();
        for (CommandShortcut s : old) {
            if (s != shortcut) {
                newList.add(s);
            }
        }
        shortcuts = newList.toArray(CommandShortcut[]::new);
    }
}
