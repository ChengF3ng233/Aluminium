package cn.feng.aluminium.module.modules.visual.hud;

import cn.feng.aluminium.module.Module;
import cn.feng.aluminium.module.ModuleCategory;
import cn.feng.aluminium.util.animation.advanced.composed.ColorAnimation;
import cn.feng.aluminium.value.impl.ModeValue;
import cn.feng.aluminium.value.impl.NoteValue;

import java.awt.*;
import java.util.Arrays;

public class HUD extends Module {
    // Accent Colors
    private static final AccentColor[] accentColors = {
            new AccentColor(new Color(26, 188, 156), "Turquoise"),
            new AccentColor(new Color(22, 160, 133), "GreenSea"),
            new AccentColor(new Color(241, 196, 15), "SunFlower"),
            new AccentColor(new Color(243, 156, 18), "Orange"),
            new AccentColor(new Color(46, 204, 113), "Emerald"),
            new AccentColor(new Color(39, 174, 96), "Nephritis"),
            new AccentColor(new Color(230, 126, 34), "Carrot"),
            new AccentColor(new Color(211, 84, 0), "Pumpkin"),
            new AccentColor(new Color(52, 152, 219), "PeterRiver"),
            new AccentColor(new Color(41, 128, 185), "BelizeHole"),
            new AccentColor(new Color(231, 76, 60), "Alirizarin"),
            new AccentColor(new Color(192, 57, 43), "Pomegranate"),
            new AccentColor(new Color(155, 89, 182), "Amethyst"),
            new AccentColor(new Color(142, 68, 173), "Wisteria")
    };
    private static final ColorAnimation accentColor = new ColorAnimation(accentColors[0].getColor(), accentColors[0].getColor(), 500);

    // Settings
    private static final NoteValue generalSetting = new NoteValue("General Setting");
    private static final ModeValue accentColorValue = new ModeValue("Accent Color", accentColors[0].getName(), Arrays.stream(accentColors).map(AccentColor::getName).toArray(String[]::new));


    // Change action
    static {
        accentColorValue.setOnChange((oldValue, newValue) -> {
            accentColor.change(findColorByName(newValue).getColor());
        });
    }

    public HUD() {
        super("HUD", ModuleCategory.VISUAL);
    }

    public static Color getAccentColor() {
        return accentColor.getOutput();
    }

    private static AccentColor findColorByName(String name) {
        for (AccentColor accentColor : accentColors) {
            if (accentColor.getName().equalsIgnoreCase(name)) {
                return accentColor;
            }
        }
        return accentColors[0]; // 如果找不到，返回第一个
    }
}
