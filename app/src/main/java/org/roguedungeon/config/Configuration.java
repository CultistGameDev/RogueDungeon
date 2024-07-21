package org.roguedungeon.config;

import java.io.Serializable;

public record Configuration(int windowWidth, int windowHeight, String windowTitle) implements Serializable {
    public static Configuration config = null;
    public static Configuration config() {
        if (config == null) {
            config = new Configuration(1280, 720, "Rogue Dungeon");
        }
        return config;
    }
}
