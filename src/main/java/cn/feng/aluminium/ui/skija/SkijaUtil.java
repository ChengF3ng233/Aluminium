package cn.feng.aluminium.ui.skija;

import io.github.humbleui.skija.Paint;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class SkijaUtil {
    public static Paint getColor(Color color) {
        return new Paint().setColor(color.getRGB());
    }
}
