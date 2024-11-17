package cn.feng.aluminium.ui.nanovg;

import cn.feng.aluminium.util.render.RenderUtil;

import java.awt.*;

public class RollingText {
    private float addition;
    private NanoFontRenderer font;

    public RollingText(NanoFontRenderer font) {
        this.font = font;
    }

    public void drawString(String text, float x, float y, float width, float size, Color color) {
        if (font == null) return;
        float strWidth = font.getStringWidth(text, size);
        addition += (float) (RenderUtil.frameTime * 0.1f);
        if (addition > strWidth) {
            addition = -width;
        }
        NanoUtil.scissorStart(x, y - 1f, width, font.getHeight(text, size) + 2f);
        font.drawString(text, strWidth > width ? x - addition : x, y, size, color);
        NanoUtil.scissorEnd();
    }

    public void setFont(NanoFontRenderer font) {
        this.font = font;
    }
}
