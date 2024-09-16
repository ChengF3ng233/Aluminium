package cn.feng.aluminium.ui.font;

import cn.feng.aluminium.ui.skija.SkijaUtil;
import cn.feng.aluminium.util.Util;
import cn.feng.aluminium.util.data.IOUtil;
import cn.feng.aluminium.util.data.ResourceType;
import cn.feng.aluminium.util.data.ResourceUtil;
import io.github.humbleui.skija.*;
import io.github.humbleui.skija.paragraph.*;
import io.github.humbleui.skija.shaper.Shaper;
import io.github.humbleui.skija.shaper.ShapingOptions;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.io.IOException;
import java.io.InputStream;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class SkijaFontRenderer extends Util {
    private final Typeface typeface;
    private final Font font;

    public SkijaFontRenderer(String resourceName) {
        try (InputStream inputStream = ResourceUtil.getResourceAsStream(resourceName + ".ttf", ResourceType.FONT)) {
            byte[] byteArray = IOUtil.toByteArray(inputStream);
            typeface = Typeface.makeFromData(Data.makeFromBytes(byteArray));
            font = new Font(typeface, 25);
            font.setSubpixel(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public float getHeight() {
        FontMetrics metrics = font.getMetrics();
        return metrics.getHeight();
    }

    private Image createTextImage(String text, float size, java.awt.Color color) {
        font.setSize(size);
        Shaper shaper = Shaper.makeShapeDontWrapOrReorder();
        TextLine textLine = shaper.shapeLine(text, font);

        Surface surface = Surface.makeRaster(
                new ImageInfo(
                        (int) textLine.getWidth() + 6,
                        (int) getHeight() / 2 + 6,
                        ColorType.RGBA_8888,
                        ColorAlphaType.UNPREMUL
                )
        );

        Canvas canvas = surface.getCanvas();

        Paint paint = SkijaUtil.getColor(color);
        paint.setAntiAlias(true);
        canvas.drawTextLine(textLine, 0, getHeight() / 2f + 3f, paint);
        paint.setMaskFilter(MaskFilter.makeBlur(FilterBlurMode.SOLID, 2f));
        canvas.drawTextLine(textLine, 0, getHeight() / 2f + 3f, paint);

        Image image = surface.makeImageSnapshot();

        paint.close();
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

    public void drawString(String text, float x, float y, float size, java.awt.Color color) {
        ScaledResolution sr = new ScaledResolution(mc);
        Image textImage = createTextImage(text, size, color);
        int texture = createTexture(textImage);

        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        glBindTexture(GL_TEXTURE_2D, texture);
        glBegin(GL_QUADS);
        // 修正纹理坐标和顶点坐标，翻转 Y 轴
        glTexCoord2f(0.0f, 1.0f);  // 左上角
        glVertex2f(x, y + (float) textImage.getHeight() / sr.getScaleFactor());

        glTexCoord2f(1.0f, 1.0f);  // 右上角
        glVertex2f(x + (float) textImage.getWidth() / sr.getScaleFactor(), y + (float) textImage.getHeight() / sr.getScaleFactor());

        glTexCoord2f(1.0f, 0.0f);  // 右下角
        glVertex2f(x + (float) textImage.getWidth() / sr.getScaleFactor(), y);

        glTexCoord2f(0.0f, 0.0f);  // 左下角
        glVertex2f(x, y);
        glEnd();

        glDeleteTextures(texture);
    }
}
