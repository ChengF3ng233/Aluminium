package cn.feng.aluminium.ui.font;

import cn.feng.aluminium.ui.skija.SkijaUtil;
import cn.feng.aluminium.util.Util;
import cn.feng.aluminium.util.data.IOUtil;
import cn.feng.aluminium.util.data.resource.ResourceType;
import cn.feng.aluminium.util.data.resource.ResourceUtil;
import cn.feng.aluminium.util.render.RenderUtil;
import io.github.humbleui.skija.*;
import io.github.humbleui.skija.shaper.Shaper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class SkijaFontRenderer extends Util {
    private Font font;
    private final SkijaFontRenderer boldRenderer;

    private final List<SkijaFontCache> cacheList = new ArrayList<>();

    public SkijaFontRenderer(String resourceName) {
        try (InputStream inputStream = ResourceUtil.getResourceAsStream(resourceName + ".ttf", ResourceType.FONT)) {
            byte[] byteArray = IOUtil.toByteArray(inputStream);
            Typeface typeface = Typeface.makeFromData(Data.makeFromBytes(byteArray));
            font = new Font(typeface, 25);
            font.setSubpixel(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            boldRenderer = new SkijaFontRenderer(resourceName, font);
        }
    }

    private SkijaFontRenderer(String resourceName, Font plainFont) {
        try (InputStream inputStream = ResourceUtil.getResourceAsStream(resourceName + "-bold.ttf", ResourceType.FONT)) {
            byte[] byteArray = IOUtil.toByteArray(inputStream);
            Typeface typeface = Typeface.makeFromData(Data.makeFromBytes(byteArray));
            font = new Font(typeface, 25);
            font.setSubpixel(true);
        } catch (NullPointerException e) {
            font = plainFont;
            font.setSubpixel(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            boldRenderer = this;
        }
    }

    public SkijaFontRenderer bold() {
        return boldRenderer;
    }

    public float getHeight() {
        FontMetrics metrics = font.getMetrics();
        return metrics.getHeight();
    }

    public float getHeight(float size) {
        float oldSize = font.getSize();
        font.setSize(size);
        FontMetrics metrics = font.getMetrics();
        float height = metrics.getHeight();
        font.setSize(oldSize);
        return height;
    }

    public float getStringWidth(String text, float size) {
        font.setSize(size);
        Shaper shaper = Shaper.makeShapeDontWrapOrReorder();
        TextLine textLine = shaper.shapeLine(text, font);
        shaper.close();
        return textLine.getWidth();
    }

    public float getCenterCorrectedPos(String text, float size, float centerX) {
        return centerX - getStringWidth(text, size) / 2f;
    }

    private Image createTextImage(String text, float size, java.awt.Color color, boolean glow, boolean shadow) {
        font.setSize(size);
        Shaper shaper = Shaper.makeShapeDontWrapOrReorder();
        TextLine textLine = shaper.shapeLine(text, font);
        ScaledResolution sr = new ScaledResolution(mc);

        Surface surface = Surface.makeRaster(
                new ImageInfo(
                        (int) (textLine.getWidth()),
                        (int) (textLine.getHeight()),
                        ColorType.RGBA_8888,
                        ColorAlphaType.UNPREMUL
                )
        );

        Canvas canvas = surface.getCanvas();

        Paint paint = SkijaUtil.getColor(color);
        Paint shadowPaint = SkijaUtil.getColor(new java.awt.Color(20, 20, 20, color.getAlpha()));
        paint.setAntiAlias(true);
        shadowPaint.setAntiAlias(true);

        if (shadow) {
            canvas.drawTextLine(textLine, 0.5f, getHeight() / 2f + 3.5f, shadowPaint);
        }

        canvas.drawTextLine(textLine, 0, getHeight() / 2f + 3f, paint);

        if (glow) {
            paint.setMaskFilter(MaskFilter.makeBlur(FilterBlurMode.SOLID, 2f));
            canvas.drawTextLine(textLine, 0, getHeight() / 2f + 3f, paint);
        }

        Image image = surface.makeImageSnapshot();

        paint.close();
        shadowPaint.close();
        shaper.close();
        surface.close();
        return image;
    }

    private int createTexture(Image image) {
        // 生成纹理 ID
        int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        // 设置纹理参数
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        // 上传纹理数据
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image.peekPixels());

        // 解绑纹理
        glBindTexture(GL_TEXTURE_2D, 0);

        return textureID;
    }

    private SkijaFontCache getFontCache(String text, float size, java.awt.Color color, boolean glow, boolean shadow) {
        for (SkijaFontCache cache : cacheList) {
            if (cache.match(text, color, size, glow, shadow)) {
                return cache;
            }
        }

        Image textImage = createTextImage(text, size, color, glow, shadow);
        int texture = createTexture(textImage);
        SkijaFontCache cache = new SkijaFontCache(text, size, glow, shadow, color, textImage, texture);
        cacheList.add(cache);

        return cache;
    }

    private void drawImage(int texture, float x, float y, Image textImage) {
        ScaledResolution sr = new ScaledResolution(mc);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        RenderUtil.drawTexturedRect(texture, x, y, (float) textImage.getWidth() / sr.getScaleFactor(), (float) textImage.getHeight() / sr.getScaleFactor());
    }

    public void drawString(String text, float x, float y, float size, java.awt.Color color, boolean shadow) {
        SkijaFontCache fontCache = getFontCache(text, size, color, false, shadow);
        Image textImage = fontCache.getTextImage();
        int texture = fontCache.getTexture();
        drawImage(texture, x, y, textImage);
    }

    public void drawCenteredString(String text, float x, float y, float size, java.awt.Color color, boolean shadow) {
        ScaledResolution sr = new ScaledResolution(mc);
        SkijaFontCache fontCache = getFontCache(text, size, color, false, shadow);
        Image textImage = fontCache.getTextImage();
        int texture = fontCache.getTexture();
        drawImage(texture, x - (float) textImage.getWidth() / sr.getScaleFactor() / 2f, y, textImage);
    }

    public void drawGlowString(String text, float x, float y, float size, java.awt.Color color, boolean shadow) {
        SkijaFontCache fontCache = getFontCache(text, size, color, true, shadow);
        Image textImage = fontCache.getTextImage();
        int texture = fontCache.getTexture();
        drawImage(texture, x, y, textImage);
    }
}
