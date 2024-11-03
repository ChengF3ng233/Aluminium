package cn.feng.aluminium.ui.nanovg;

import cn.feng.aluminium.util.Util;
import cn.feng.aluminium.util.data.IOUtil;
import cn.feng.aluminium.util.data.ResourceType;
import cn.feng.aluminium.util.data.ResourceUtil;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static cn.feng.aluminium.ui.nanovg.NanoLoader.ctx;
import static cn.feng.aluminium.ui.nanovg.NanoUtil.createGradient;
import static org.lwjgl.nanovg.NanoVG.*;

/**
 * @author ChengFeng
 * @since 2024/8/3
 **/
public class NanoFontRenderer extends Util {
    private final String name;
    private int font;
    private final NanoFontRenderer boldRenderer;
    private float size;

    public String getName() {
        return name;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public NanoFontRenderer(String name, String fileName) {
        this.name = name;
        try (InputStream inputStream = ResourceUtil.getResourceAsStream(fileName + ".ttf", ResourceType.FONT)) {
            byte[] data = IOUtil.toByteArray(inputStream);
            ByteBuffer buffer = MemoryUtil.memAlloc(data.length);
            buffer.put(data).flip();

            font = nvgCreateFontMem(ctx, name, buffer, false);
            size = 16;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        boldRenderer = new NanoFontRenderer(name, fileName, font);
    }

    private NanoFontRenderer(String name, String fileName, int plainFont) {
        this.name = name + "-bold";
        try (InputStream inputStream = ResourceUtil.getResourceAsStream(fileName + "-bold.ttf", ResourceType.FONT)) {
            byte[] data = IOUtil.toByteArray(inputStream);
            ByteBuffer buffer = MemoryUtil.memAlloc(data.length);
            buffer.put(data).flip();

            font = nvgCreateFontMem(ctx, this.name, buffer, false);
            size = 16;
        } catch (NullPointerException ee) {
            font = plainFont;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        boldRenderer = this;
    }

    public NanoFontRenderer bold() {
        return boldRenderer;
    }

    public float getHeight(float size) {
        nvgFontFaceId(ctx, font);
        nvgFontSize(ctx, size);
        float[] bounds = new float[4];
        nvgTextBounds(ctx, 0, 0, "AaBbCcDdEeFfGgJjYy", bounds);
        return (bounds[3] - bounds[1]) / 2f;
    }

    public float getHeight(String text, float size) {
        if (text == null) return 0f;
        nvgFontFaceId(ctx, font);
        nvgFontSize(ctx, size);
        float[] bounds = new float[4];
        nvgTextBounds(ctx, 0, 0, text, bounds);
        return (bounds[3] - bounds[1]) / 2f;
    }

    // Plain
    public void drawString(String text, float x, float y, float size, int align, Color color) {
        renderPlainString(text, x, y, size, align, color);
    }

    public void drawString(String text, float x, float y, int align, Color color) {
        renderPlainString(text, x, y, size, align, color);
    }

    public void drawString(String text, float x, float y, int align, Color color, boolean shadow) {
        drawString(text, x, y, size, align, color, shadow);
    }

    public void drawString(String text, float x, float y, float size, int align, Color color, boolean shadow) {
        if (shadow) {
            renderPlainString(text, x + 0.5f, y + 0.5f, size, align, new Color(20, 20, 20, color.getAlpha()));
        }

        renderPlainString(text, x, y, size, align, color);
    }

    public void drawBlurString(String text, float x, float y, float size, float radius, int align, Color color, boolean shadow) {
        if (shadow) {
            renderBlurString(text, x + 0.5f, y + 0.5f, size, radius, align, new Color(20, 20, 20, color.getAlpha()));
        }

        renderBlurString(text, x, y, size, radius, align, color);
    }

    public void drawString(String text, float x, float y, float size, Color color, boolean shadow) {
        int rgb = color.getRGB();

        if (shadow) {
            rgb = (rgb & 16579836) >> 2 | rgb & -16777216;
        }

        if (shadow) {
            renderPlainString(text, x + 0.5f, y + 0.5f, size, NVG_ALIGN_LEFT | NVG_ALIGN_TOP, new Color(rgb));
        }
        renderPlainString(text, x, y, size, NVG_ALIGN_LEFT | NVG_ALIGN_TOP, color);
    }

    public void drawString(String text, float x, float y, float size, Color color) {
        renderPlainString(text, x, y, size, NVG_ALIGN_LEFT | NVG_ALIGN_TOP, color);
    }

    public void drawString(String text, float x, float y, Color color) {
        renderPlainString(text, x, y, size, NVG_ALIGN_LEFT | NVG_ALIGN_TOP, color);
    }

    public void drawString(String text, float x, float y, Color col, boolean shadow) {
        drawString(text, x, y, size, NVG_ALIGN_LEFT | NVG_ALIGN_TOP, col, shadow);
    }

    // Glow
    public void drawGlowString(String text, float x, float y, Color color) {
        renderGlowString(text, x, y, size, 3f, NVG_ALIGN_LEFT | NVG_ALIGN_TOP, color, color);
    }

    public void drawGlowString(String text, float x, float y, Color color, boolean shadow) {
        int rgb = color.getRGB();

        if (shadow) {
            rgb = (rgb & 16579836) >> 2 | rgb & -16777216;
        }
        renderPlainString(text, x + 0.5f, y + 0.5f, size, NVG_ALIGN_LEFT | NVG_ALIGN_TOP, new Color(rgb));
        drawGlowString(text, x, y, color);
    }

    public void drawGlowString(String text, float x, float y, float size, Color color) {
        renderGlowString(text, x, y, size, 3f, NVG_ALIGN_LEFT | NVG_ALIGN_TOP, color, color);
    }

    public void drawGlowString(String text, float x, float y, float size, int align, Color color) {
        renderGlowString(text, x, y, size, 3f, align, color, color);
    }

    public void drawGlowString(String text, float x, float y, float size, float radius, int align, Color color) {
        renderGlowString(text, x, y, size, radius, align, color, color);
    }

    public void drawGlowString(String text, float x, float y, float size, int align, Color textColor, Color glowColor) {
        renderGlowString(text, x, y, size, 3f, align, textColor, glowColor);
    }

    public void drawGlowString(String text, float x, float y, float size, Color textColor, Color glowColor) {
        renderGlowString(text, x, y, size, 3f, NVG_ALIGN_LEFT | NVG_ALIGN_TOP, textColor, glowColor);
    }

    public void drawGradientString(String text, float x, float y, float size, int align, Color left, Color right) {
        renderGradientString(text, x, y, x, y, x + getStringWidth(text, size), y + getHeight(text, size), size, align, left, right);
    }

    public void drawGradientString(String text, float x, float y, float gradientStartX, float gradientStartY, float gradientEndX, float gradientEndY, float size, int align, Color left, Color right) {
        renderGradientString(text, x, y, gradientStartX, gradientStartY, gradientEndX, gradientEndY, size, align, left, right);
    }

    private void renderPlainString(String text, float x, float y, float size, int align, Color color) {
        nvgTextAlign(ctx, align);

        NanoUtil.fillColor(color);

        renderString(text, x, y + 1f, size);
    }

    private void renderGradientString(String text, float x, float y, float gradientStartX, float gradientStartY, float gradientEndX, float gradientEndY, float size, int align, Color left, Color right) {
/*        nvgTextAlign(ctx, align);

        NVGPaint gradient = NanoUtil.createGradient(gradientStartX, gradientStartY, gradientEndX, gradientEndY, left, right);

        nvgFillPaint(ctx, gradient);
        renderString(text, x, y + 1f, size);

        gradient.free();*/
        nvgTextAlign(ctx, align);
        // 计算文本宽度
        float textWidth = getStringWidth(text, size);

        // 定义渐变的坐标
        gradientStartX = x; // 渐变开始于文本的起始位置
        gradientEndX = x + textWidth; // 渐变结束于文本的宽度
        gradientStartY = y - size / 2; // 根据文本大小调整 y 坐标
        gradientEndY = gradientStartY; // 这里保持 y 坐标不变，形成水平渐变

        // 创建渐变
        NVGPaint gradient = createGradient(gradientStartX, gradientStartY, gradientEndX, gradientEndY, Color.YELLOW, Color.WHITE);

        // 设置渐变填充
        nvgFillPaint(ctx, gradient);
        nvgRect(ctx, x, y, textWidth, size);

        // 绘制文本
        renderString(text, x, y, size);
        nvgFill(ctx);

        // 释放资源
        gradient.free();
    }

    private void renderBlurString(String text, float x, float y, float size, float radius, int align, Color color) {
        nvgTextAlign(ctx, align);

        NanoUtil.fillColor(color);
        nvgFontBlur(ctx, radius);
        renderString(text, x, y + 1f, size);
    }

    private void renderGlowString(String text, float x, float y, float size, float radius, int align, Color textColor, Color glowColor) {
        nvgTextAlign(ctx, align);

        NanoUtil.fillColor(glowColor);
        nvgFontBlur(ctx, radius);
        renderString(text, x, y + 1f, size);

        NanoUtil.fillColor(textColor);
        nvgFontBlur(ctx, 0f);
        renderString(text, x, y + 1f, size);
    }

    private void renderString(String text, float x, float y, float size) {
        nvgFontFaceId(ctx, font);
        nvgFontSize(ctx, size / 2f);
        nvgText(ctx, x, y, text);
    }

    /**
     * @param text 文本
     * @return 文本长度
     */
    public float getStringWidth(String text) {
        return getStringWidth(text, size);
    }

    /**
     * @param text 文本
     * @param size 字体大小
     * @return 文本长度
     */
    public float getStringWidth(String text, float size) {
        if (text == null) return 0f;
        nvgFontFaceId(ctx, font);
        nvgFontSize(ctx, size);
        float[] bounds = new float[4];
        nvgTextBounds(ctx, 0, 0, text, bounds);
        return (bounds[2] - bounds[0]) / 2f;
    }

    public String trimStringToWidth(String text, float width, float size) {
        return this.trimStringToWidth(text, width, size, false, false);
    }

    public String trimStringToWidth(String text, float width) {
        return this.trimStringToWidth(text, width, size, false, false);
    }

    public String trimStringToWidth(String text, float width, boolean reverse) {
        return this.trimStringToWidth(text, width, size, reverse, false);
    }

    public String trimStringToWidth(String text, float width, float size, boolean reverse, boolean more) {
        if (text == null) {
            return "";
        } else {
            StringBuilder builder = new StringBuilder();
            int startIndex = reverse ? text.length() - 1 : 0;
            int step = reverse ? -1 : 1;

            String nextChar = "";
            for (int i = startIndex; i <= text.length() - 1 && i >= 0 && getStringWidth(builder + nextChar, size) <= width; i += step) {
                builder.append(text.charAt(i));
                nextChar = reverse ? (i == 0 ? "" : String.valueOf(text.charAt(i + step))) : (i == text.length() - 1 ? "" : String.valueOf(text.charAt(i + step)));
            }

            if (reverse) builder.reverse();
            String result = builder.toString();
            if (more && !text.equals(result)) {
                result = reverse ? "..." + result : result + "...";
            }
            return result;
        }
    }
}
