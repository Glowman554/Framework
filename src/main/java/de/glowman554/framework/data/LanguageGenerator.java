package de.glowman554.framework.data;

import de.glowman554.framework.client.utils.DirectoryUtils;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

import java.io.File;
import java.io.IOException;

public abstract class LanguageGenerator {
    private final JsonNode root = JsonNode.object();
    private final String id;

    public LanguageGenerator(String id) {
        this.id = id;
        generateTranslations();
        save();
    }

    public abstract void generateTranslations();

    public void addTranslation(String key, String value) {
        root.set(key, value);
    }

    private void save() {

        try {
            DirectoryUtils.createDirectory(new File("generated"));
            DirectoryUtils.createDirectory(new File("generated/lang"));
            Json.json().serialize(root, new File("generated/lang/" + id + ".json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
