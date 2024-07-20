package org.roguedungeon;

public class GameOptions {
    private final int windowWidth, windowHeight;

    public static GameOptions defaultOptions() {
        return new GameOptions(1280, 720);
    }

    public GameOptions(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }
}
