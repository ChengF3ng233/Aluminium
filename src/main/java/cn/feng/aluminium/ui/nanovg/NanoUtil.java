package cn.feng.aluminium.ui.nanovg;

import cn.feng.aluminium.config.ConfigManager;
import cn.feng.aluminium.util.Util;
import cn.feng.aluminium.util.data.IOUtil;
import cn.feng.aluminium.util.misc.ChatUtil;
import cn.feng.aluminium.util.render.GLUtil;
import cn.feng.aluminium.util.render.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.system.MemoryUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static cn.feng.aluminium.ui.nanovg.NanoLoader.ctx;
import static org.lwjgl.nanovg.NanoVG.*;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class NanoUtil extends Util {
    public static final Map<String, Integer> imageMap = new HashMap<>();

    public static void beginFrame() {
        nvgBeginFrame(ctx, mc.displayWidth, mc.displayHeight, 1f);
        ScaledResolution sr = new ScaledResolution(mc);
        scaleStart(0, 0, sr.getScaleFactor());
    }

    public static void endFrame() {
        nvgEndFrame(ctx);
        RenderUtil.setAlphaLimit(0);
        GLUtil.startBlend();
    }

    public static void beginPath() {
        nvgBeginPath(ctx);
    }

    public static void closePath() {
        nvgClosePath(ctx);
    }

    public static void drawRoundedRect(float x, float y, float width, float height, float radius, Color color) {
        beginPath();
        nvgRoundedRect(ctx, x, y, width, height, radius);
        fillColor(color);
        nvgFill(ctx);
    }

    public static void drawRect(float x, float y, float width, float height, Color color) {
        beginPath();
        nvgRect(ctx, x, y, width, height);
        fillColor(color);
        nvgFill(ctx);
    }

    public static void drawRoundedRect(float x, float y, float width, float height, float leftTopRadius, float rightTopRadius, float rightBottomRadius, float leftBottomRadius, Color color) {
        beginPath();
        nvgRoundedRectVarying(ctx, x, y, width, height, leftTopRadius, rightTopRadius, rightBottomRadius, leftBottomRadius);
        fillColor(color);
        nvgFill(ctx);
    }

    public static NVGColor getColor(Color color) {
        NVGColor nvgColor = NVGColor.calloc();
        nvgColor.r(color.getRed() / 255f).g(color.getGreen() / 255f).b(color.getBlue() / 255f).a(color.getAlpha() / 255f);
        return nvgColor;
    }

    public static int genImageId(InputStream inputStream) {
        byte[] data;
        try {
            data = IOUtil.toByteArray(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ByteBuffer buffer = MemoryUtil.memAlloc(data.length);
        buffer.put(data).flip();

        return nvgCreateImageMem(ctx, 0, buffer);
    }

    public static int genImageId(BufferedImage image) {
        File cacheFile = new File(ConfigManager.cacheDir, ThreadLocalRandom.current().nextFloat() + ".png");
        try {
            ImageIO.write(image, "png", cacheFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return nvgCreateImage(ctx, cacheFile.getAbsolutePath(), 0);
    }

    public static int genImageId(File file) {
        return nvgCreateImage(ctx, file.getAbsolutePath(), 0);
    }

    public static void drawImageRect(ResourceLocation location, float x, float y, float width, float height) {
        int imageId = 0;
        if (imageMap.containsKey(location.getResourcePath())) {
            imageId = imageMap.get(location.getResourcePath());
        } else {
            try {
                InputStream inputStream = mc.getResourceManager().getResource(location).getInputStream();
                imageId = genImageId(inputStream);
            } catch (IOException e) {

            }
        }

        if (imageId == -1 || imageId == 0) {
            // 处理图像加载失败的情况
            return;
        }

        drawImageRect(imageId, x, y, width, height);

        imageMap.put(location.getResourcePath(), imageId);
    }

    public static void drawImageRect(ResourceLocation location, float x, float y, float width, float height, Color color) {
        int imageId = 0;
        if (imageMap.containsKey(location.getResourcePath())) {
            imageId = imageMap.get(location.getResourcePath());
        } else {
            try {
                InputStream inputStream = mc.getResourceManager().getResource(location).getInputStream();
                imageId = genImageId(inputStream);
            } catch (IOException e) {

            }
        }

        if (imageId == -1 || imageId == 0) {
            // 处理图像加载失败的情况
            return;
        }

        drawImageRect(imageId, x, y, width, height, color);

        imageMap.put(location.getResourcePath(), imageId);
    }

    public static void drawImageRect(BufferedImage image, float x, float y, float width, float height, Color color) {
        int imageId = 0;
        if (imageMap.containsKey(image.toString())) {
            imageId = imageMap.get(image.toString());
        } else {
            imageId = genImageId(image);
        }

        if (imageId == -1 || imageId == 0) {
            // 处理图像加载失败的情况
            return;
        }

        drawImageRect(imageId, x, y, width, height, color);

        imageMap.put(image.toString(), imageId);
    }

    public static void drawImageRect(int imageId, float x, float y, float width, float height) {
        NVGPaint imgPaint = NVGPaint.calloc();

        nvgBeginPath(ctx);  // 开始一个新的路径
        nvgRect(ctx, x, y, width, height);  // 定义一个矩形区域用于绘制图像
        nvgImagePattern(ctx, x, y, width, height, 0, imageId, 1.0f, imgPaint);
        nvgFillPaint(ctx, imgPaint);  // 设置填充样式为图像
        nvgFill(ctx);  // 填充图像

        imgPaint.free();
    }

    public static void drawGradientRect(float x, float y, float width, float height, Color left, Color right){
        NVGPaint gradient = createGradient(x, y, x + width, y + height, left, right);
        beginPath();
        nvgRect(ctx, x, y, width, height);
        nvgFillPaint(ctx, gradient);
        nvgFill(ctx);
    }

    public static void drawImageRect(int imageId, float x, float y, float width, float height, Color color) {
        NVGPaint imgPaint = NVGPaint.calloc();

        nvgBeginPath(ctx);  // 开始一个新的路径
        nvgRect(ctx, x, y, width, height);  // 定义一个矩形区域用于绘制图像
        nvgImagePattern(ctx, x, y, width, height, 0, imageId, 1.0f, imgPaint);
        fillColor(color);
        nvgFillPaint(ctx, imgPaint);  // 设置填充样式为图像
        nvgFill(ctx);  // 填充图像

        imgPaint.free();
    }

    public static void drawShadowRound(float x, float y, float width, float height, float radius) {
        NVGPaint paint = NVGPaint.calloc();
        nvgBoxGradient(ctx, x, y, width, height, radius, 5f, getColor(new Color(0, 0, 0, 255)), getColor(new Color(0, 0, 0, 0)), paint);
        nvgBeginPath(ctx);
        nvgRect(ctx, x - 5, y - 5, width + 10, height + 10);
        nvgRoundedRect(ctx, x, y, width, height, radius);
        nvgPathWinding(ctx, NVG_HOLE);
        nvgFillPaint(ctx, paint);
        nvgFill(ctx);
        paint.free();
    }

    public static void drawImageRound(BufferedImage image, float x, float y, float width, float height, float radius) {
        NVGPaint imgPaint = NVGPaint.calloc();
        String key = image.toString();
        int texture = imageMap.containsKey(key) ? imageMap.get(key) : genImageId(image);

        nvgBeginPath(ctx);
        nvgRoundedRect(ctx, x, y, width, height, radius);
        nvgImagePattern(ctx, x, y, width, height, 0, texture, 1.0f, imgPaint);
        nvgFillPaint(ctx, imgPaint);
        nvgFill(ctx);

        imgPaint.free();
        if (!imageMap.containsKey(key)) {
            imageMap.put(key, texture);
        }
    }

    public static NVGPaint createGradient(float startX, float startY, float endX, float endY, Color left, Color right) {
        NVGPaint paint = NVGPaint.calloc();
        nvgLinearGradient(ctx, startX, startY, endX, endY, getColor(right), getColor(left), paint);
        return paint;
    }

    public static void drawImageCircle(String name, InputStream inputStream, float x, float y, float radius) {
        int imageId = imageMap.containsKey(name) ? imageMap.get(name) : genImageId(inputStream);

        if (imageId == -1 || imageId == 0) {
            // 处理图像加载失败的情况
            return;
        }

        drawImageCircle(imageId, x, y, radius);

        imageMap.put(name, imageId);
    }

    public static void drawImageCircle(BufferedImage image, float x, float y, float radius) {
        int imageId = imageMap.containsKey(image.toString()) ? imageMap.get(image.toString()) : genImageId(image);

        if (imageId == -1 || imageId == 0) {
            // 处理图像加载失败的情况
            return;
        }

        drawImageCircle(imageId, x, y, radius);

        imageMap.put(image.toString(), imageId);
    }

    public static void drawImageCircle(int imageId, float x, float y, float radius) {
        drawImageCircle(imageId, x, y, radius, 0f);
    }

    public static void drawImageCircle(int imageId, float x, float y, float radius, float angle) {
        NVGPaint imgPaint = NVGPaint.calloc();

        rotateStart(x, y, angle);
        nvgBeginPath(ctx);  // 开始一个新的路径
        nvgCircle(ctx, x, y, radius);  // 定义一个圆形区域用于绘制图像
        nvgImagePattern(ctx, x - radius, y - radius, radius * 2f, radius * 2f, 0f, imageId, 1.0f, imgPaint);
        nvgFillPaint(ctx, imgPaint);  // 设置填充样式为图像
        nvgFill(ctx);  // 填充图像
        rotateEnd();

        imgPaint.free();
    }

    public static void drawRoundedOutlineRect(float x, float y, float width, float height, float radius, float outlineWidth, Color fillColor, Color outlineColor) {
        drawRoundedRect(x, y, width, height, radius, fillColor);
        strokeColor(outlineColor);
        nvgStrokeWidth(ctx, outlineWidth);
        nvgStroke(ctx);
    }

    public static void drawCircle(float centerX, float centerY, float radius, Color color) {
        beginPath();
        nvgCircle(ctx, centerX, centerY, radius);
        fillColor(color);
        nvgFill(ctx);
    }

    public static void scaleStart(float centerX, float centerY, float scale) {
        nvgSave(ctx);
        nvgTranslate(ctx, centerX, centerY);
        nvgScale(ctx, scale, scale);
        nvgTranslate(ctx, -centerX, -centerY);
    }

    public static void scaleXStart(float centerX, float centerY, float scale) {
        nvgSave(ctx);
        nvgTranslate(ctx, centerX, centerY);
        nvgScale(ctx, scale, 1f);
        nvgTranslate(ctx, -centerX, -centerY);
    }

    public static void scaleEnd() {
        nvgRestore(ctx);
    }

    /**
     * 绘制折线（Polyline）
     *
     * @param points    折线顶点数组，每两个浮点数为一个点 (x, y)
     * @param color     线条颜色
     * @param lineWidth 线条宽度
     */
    public static void drawPolyline(float[] points, int color, float lineWidth) {
        if (points.length < 4) {
            throw new IllegalArgumentException("At least two points are required to render a line.");
        }

        // 开始绘制路径
        nvgBeginPath(ctx);

        // 设置线条颜色和宽度
        strokeColor(new Color(color));
        nvgStrokeWidth(ctx, lineWidth);

        // 移动到第一个点
        nvgMoveTo(ctx, points[0], points[1]);

        // 添加折线的每个点
        for (int i = 2; i < points.length; i += 2) {
            nvgLineTo(ctx, points[i], points[i + 1]);
        }

        // 描边路径
        nvgStroke(ctx);
    }

    public static void rotateStart(float centerX, float centerY, float angle) {
        // 保存当前状态
        nvgSave(ctx);

        // 移动到绘制区域的中心点
        nvgTranslate(ctx, centerX, centerY);
        nvgRotate(ctx, nvgDegToRad(angle));
        nvgTranslate(ctx, -centerX, -centerY);
    }

    public static void rotateEnd() {
        nvgRestore(ctx);
    }

    public static void transformStart(float x, float y) {
        nvgSave(ctx);
        nvgTranslate(ctx, x, y);
    }

    public static void transformEnd() {
        nvgRestore(ctx);
    }

    public static void scissorStart(float x, float y, float width, float height) {
        scissorStart(x, y, width, height, true);
    }

    public static void scissorStart(float x, float y, float width, float height, boolean reset) {
        nvgSave(ctx);
        if (reset) {
            nvgScissor(ctx, x, y, width, height);
        } else {
            nvgIntersectScissor(ctx, x, y, width, height);
        }
    }

    public static void scissorEnd() {
        nvgRestore(ctx);
    }

    public static void fillColor(Color color) {
        NVGColor nvgColor = getColor(color);
        nvgFillColor(ctx, nvgColor);
        nvgColor.free();
    }

    public static void strokeColor(Color color) {
        NVGColor nvgColor = getColor(color);
        nvgStrokeColor(ctx, nvgColor);
        nvgColor.free();
    }

}
