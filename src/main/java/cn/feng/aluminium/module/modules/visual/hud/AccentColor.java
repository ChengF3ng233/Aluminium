package cn.feng.aluminium.module.modules.visual.hud;

import java.awt.*;

public class AccentColor {
    private final Color color;
    private final String name;

    public AccentColor(Color color, String name) {
        this.color = color;
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}