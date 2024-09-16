package cn.feng.aluminium.ui.font;

import cn.feng.aluminium.ui.nano.NanoUtil;
import cn.feng.aluminium.util.data.IOUtil;
import cn.feng.aluminium.util.data.ResourceType;
import cn.feng.aluminium.util.data.ResourceUtil;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.nanovg.NanoVG.*;
import static cn.feng.aluminium.ui.nano.NanoLoader.ctx;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class NanoFontRenderer {
    private final String name;
    private final NanoFontRenderer boldRenderer;
    private int font;

    public NanoFontRenderer(String name, String resourceName) {
        this.name = name;
        try (InputStream stream = ResourceUtil.getResourceAsStream(resourceName + ".ttf", ResourceType.FONT)) {
            if (stream == null) throw new NullPointerException("Resource not found: " + resourceName);
            byte[] data = IOUtil.toByteArray(stream);
            ByteBuffer buffer = MemoryUtil.memAlloc(data.length);
            buffer.put(data).flip();
            font = nvgCreateFontMem(ctx, name, buffer, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            boldRenderer = new NanoFontRenderer(name, resourceName, font);
        }
    }

    private NanoFontRenderer(String name, String resourceName, int plainFont) {
        this.name = name + "-bold";
        try (InputStream inputStream = ResourceUtil.getResourceAsStream(resourceName + "-bold.ttf", ResourceType.FONT)) {
            if (inputStream == null) {
                font = plainFont;
                return;
            }
            byte[] data = IOUtil.toByteArray(inputStream);
            ByteBuffer buffer = MemoryUtil.memAlloc(data.length);
            buffer.put(data).flip();
            font = nvgCreateFontMem(ctx, this.name, buffer, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            boldRenderer = this;
        }
    }

    public NanoFontRenderer bold() {
        return boldRenderer;
    }

    public float getHeight(float size) {
        nvgFontFaceId(ctx, font);
        nvgFontSize(ctx, size);
        float[] bounds = new float[4];
        nvgTextBounds(ctx, 0, 0, "AaBbCcDdEeFfGgJjYy", bounds);
        return (bounds[3] - bounds[1]);
    }

    public float getHeight(String text, float size) {
        if (text == null) return 0f;
        nvgFontFaceId(ctx, font);
        nvgFontSize(ctx, size);
        float[] bounds = new float[4];
        nvgTextBounds(ctx, 0, 0, text, bounds);
        return (bounds[3] - bounds[1]);
    }

    public void drawString(String text, float x, float y, float size, Color color) {
        NanoUtil.fillColor(color);
        nvgTextAlign(ctx, NVG_ALIGN_LEFT | NVG_ALIGN_TOP);
        renderText(text, x, y, size);
    }

    private void renderText(String text, float x, float y, float size) {
        nvgFontFaceId(ctx, font);
        nvgFontSize(ctx, size);
        nvgText(ctx, x, y, text);
    }
}
