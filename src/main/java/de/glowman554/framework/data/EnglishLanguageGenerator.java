package de.glowman554.framework.data;

public class EnglishLanguageGenerator extends LanguageGenerator {
    public EnglishLanguageGenerator() {
        super("en_us");
    }

    public void generateTranslations() {
        addTranslation("key.framework.hud", "Open overlay configuration");
        addTranslation("key.framework.modselect", "Open mod selection");
        addTranslation("key.framework.misc", "Framework miscellaneous");
        addTranslation("key.framework.mods", "Framework modifications");
        addTranslation("key.framework.hide-players", "Toggle hide players");
        addTranslation("key.framework.discord-chat", "Toggle discord chat");
        addTranslation("key.framework.queue-notifier", "Toggle queue notifier");
        addTranslation("key.framework.command_shortcuts", "Open command shortcuts screen");
        addTranslation("key.framework.commands", "Command shortcuts");
        addTranslation("key.framework.auto-sprint", "Toggle auto sprint");
        addTranslation("key.framework.auto-sprint-jump", "Toggle auto sprint jump");
        addTranslation("key.framework.entity-esp", "Toggle entity ESP");
        addTranslation("key.framework.fullbright", "Toggle fullbright");
        addTranslation("key.framework.rainbow", "Toggle rainbow");
        addTranslation("key.framework.xray", "Toggle x-ray");
        addTranslation("key.framework.auto-leave", "Toggle auto leave");
    }


}
