package de.glowman554.framework.client.utils;

import java.io.File;

public class DirectoryUtils {
    public static void createDirectory(File directory) {
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new RuntimeException("Could not create data folder");
            }
        } else {
            if (!directory.isDirectory()) {
                throw new RuntimeException("Data folder is not a folder");
            }
        }
    }
}
