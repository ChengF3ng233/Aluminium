package cn.feng.aluminium.ui.music;

import cn.feng.aluminium.util.animation.advanced.composed.ColorAnimation;
import cn.feng.aluminium.util.render.ColorUtil;

import java.awt.*;

public class Theme {
    public static Color
    windowTop = ColorUtil.hexToColor("#020e3b"),
    windowBottom = ColorUtil.hexToColor("#1b1e27"),
    layerBackground = new Color(46, 46, 46, (int) (255 * 0.5)),
    shade = new Color(71, 72, 79, (int) (255 * 0.45));

    public static ColorAnimation windowTopAnim = new ColorAnimation(windowTop, windowTop, 500);
}
