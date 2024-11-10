package cn.feng.aluminium.ui.nanovg;

import cn.feng.aluminium.util.misc.ChatUtil;
import cn.feng.aluminium.util.render.RenderUtil;

import java.awt.*;

public class RollingText {
    private long startTime = System.currentTimeMillis();
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
            addition = -strWidth;
            reset();
        }
        NanoUtil.scissorStart(x, y, width, font.getHeight(text, size));
        font.drawString(text, strWidth > width? x - addition : x, y, size, color);
        NanoUtil.scissorEnd();
    }

    public void reset() {
        startTime = System.currentTimeMillis();
    }

    public void setFont(NanoFontRenderer font) {
        this.font = font;
    }
}
