package cn.feng.aluminium.ui.font;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class SkijaFontCache {
    private final String text;
    private final float size;
    private final boolean glow;
    private final boolean shadow;
    private final Color color;
    private final io.github.humbleui.skija.Image textImage;
    private final int texture;

    public SkijaFontCache(String text, float size, boolean glow, boolean shadow, Color color, io.github.humbleui.skija.Image textImage, int texture) {
        this.text = text;
        this.color = color;
        this.size = size;
        this.glow = glow;
        this.shadow = shadow;
        this.textImage = textImage;
        this.texture = texture;
    }

    public boolean match(String text, Color color, float size, boolean glow, boolean shadow) {
        return this.text.equals(text) && this.color.equals(color) && this.size == size && this.glow == glow && this.shadow == shadow;
    }

    public String getText() {
        return text;
    }

    public Color getColor() {
        return color;
    }

    public float getSize() {
        return size;
    }

    public boolean isGlow() {
        return glow;
    }

    public io.github.humbleui.skija.Image getTextImage() {
        return textImage;
    }

    public int getTexture() {
        return texture;
    }
}
