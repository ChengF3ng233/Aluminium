package cn.feng.aluminium.util.render;

import cn.feng.aluminium.util.Util;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL13;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class RenderUtil extends Util {
    private static final Map<BufferedImage, DynamicTexture> bufferedImageMap = new HashMap<>();
    private static final Map<ResourceLocation, BufferedImage> svgMap = new HashMap<>();

    // 上一帧的帧时间
    public static double lastFrame = System.nanoTime();
    public static double frameTime = 0;

    /**
     * calculates the delta frame time
     */
    public static void calcFrameDelta() {
        frameTime = ((System.nanoTime() - lastFrame) / 10000000.0);
        lastFrame = System.nanoTime();
    }

    public static void uploadTexture(BufferedImage image) {
        if (bufferedImageMap.containsKey(image)) return;
        bufferedImageMap.put(image, new DynamicTexture(image));
    }

    public static void deleteTexture(BufferedImage image) {
        if (!bufferedImageMap.containsKey(image)) return;
        bufferedImageMap.get(image).deleteGlTexture();
        bufferedImageMap.remove(image);
    }

    public static void bindTexture(BufferedImage image) {
        uploadTexture(image);
        GlStateManager.bindTexture(bufferedImageMap.get(image).getGlTextureId());
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    }

    /**
     * This will set the alpha limit to a specified value ranging from 0-1
     *
     * @param limit minimal alpha value
     */
    public static void setAlphaLimit(float limit) {
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL_GREATER, (float) (limit * .01));
    }

    public static void drawCircle(float cx, float cy, float r, int num_segments, int c) {
        glPushMatrix();
        cx *= 2.0f;
        cy *= 2.0f;
        float f = (float) (c >> 24 & 0xFF) / 255.0f;
        float f1 = (float) (c >> 16 & 0xFF) / 255.0f;
        float f2 = (float) (c >> 8 & 0xFF) / 255.0f;
        float f3 = (float) (c & 0xFF) / 255.0f;
        float theta = (float) (6.2831852 / (double) num_segments);
        float p = (float) Math.cos(theta);
        float s = (float) Math.sin(theta);
        float x = r * 2.0f;
        float y = 0.0f;
        GLUtil.setup2DRendering();
        glScalef(0.5f, 0.5f, 0.5f);
        glColor4f(f1, f2, f3, f);
        glBegin(2);
        for (int ii = 0; ii < num_segments; ++ii) {
            glVertex2f(cx, cy);
            glVertex2f(x + cx, y + cy);
            float t = x;
            x = p * x - s * y;
            y = s * t + p * y;
        }
        glEnd();
        glScalef(2.0f, 2.0f, 2.0f);
        GLUtil.end2DRendering();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        glPopMatrix();
    }

    public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
        return createFrameBuffer(framebuffer, false);
    }

    public static Framebuffer createFrameBuffer(Framebuffer framebuffer, boolean depth) {
        if (needsNewFramebuffer(framebuffer)) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }
            return new Framebuffer(mc.displayWidth, mc.displayHeight, depth);
        }
        return framebuffer;
    }

    public static void drawRect(float x, float y, float x2, float y2, int color) {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);

        color(color);
        glBegin(GL_QUADS);

        glVertex2f(x2, y);
        glVertex2f(x, y);
        glVertex2f(x, y2);
        glVertex2f(x2, y2);
        glEnd();

        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
    }

    public static void drawRectWithTexture(float left, float top, float right, float bottom) {
        glPushMatrix();
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthMask(false);
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        // 绘制矩形
        glBegin(GL_QUADS);
        glTexCoord2f(0.0f, 1.0f);
        glVertex3f(left, bottom, 0);
        glTexCoord2f(1.0f, 1.0f);
        glVertex3f(right, bottom, 0);
        glTexCoord2f(1.0f, 0.0f);
        glVertex3f(right, top, 0);
        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(left, top, 0);
        glEnd();

        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glPopMatrix();
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void drawQuads(float[] leftTop, float[] leftBottom, float[] rightTop, float[] rightBottom, Color rightColor, Color leftColor) {
        GLUtil.setup2DRendering();
        glEnable(GL_LINE_SMOOTH);
        glShadeModel(GL_SMOOTH);
        glPushMatrix();
        glBegin(GL_QUADS);
        color(leftColor.getRGB());
        glVertex2d(leftTop[0], leftTop[1]);
        glVertex2d(leftBottom[0], leftBottom[1]);
        color(rightColor.getRGB());
        glVertex2d(rightBottom[0], rightBottom[1]);
        glVertex2d(rightTop[0], rightTop[1]);
        glEnd();
        glPopMatrix();
        glDisable(GL_LINE_SMOOTH);
        GLUtil.end2DRendering();
        resetColor();
    }

    public static boolean needsNewFramebuffer(Framebuffer framebuffer) {
        return framebuffer == null || framebuffer.framebufferWidth != mc.displayWidth || framebuffer.framebufferHeight != mc.displayHeight;
    }

    /**
     * Bind a texture using the specified integer refrence to the texture.
     *
     * @see org.lwjgl.opengl.GL13 for more information about texture bindings
     */
    public static void bindTexture(int texture) {
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    public static void drawImage(ResourceLocation resourceLocation, float x, float y, float imgWidth, float imgHeight) {
        GLUtil.startBlend();

        // 绑定纹理并设置过滤参数
        mc.getTextureManager().bindTexture(resourceLocation);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);


        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, imgWidth, imgHeight, imgWidth, imgHeight);


        GLUtil.endBlend();
    }

    public static void drawSVG(ResourceLocation resourceLocation, float x, float y, float imgWidth, float imgHeight) {
        try {
            if (!svgMap.containsKey(resourceLocation)) {
                IResource resource = mc.getResourceManager().getResource(resourceLocation);
                BufferedImage image = ImageUtil.svgToBufferedImage(resource.getInputStream(), imgWidth, imgHeight);
                svgMap.put(resourceLocation, image);
            }
            drawImage(svgMap.get(resourceLocation), x, y, imgWidth, imgHeight);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void drawImage(DynamicTexture image, float x, float y, float imgWidth, float imgHeight) {
        GLUtil.startBlend();

        // 绑定纹理并设置过滤参数
        GlStateManager.bindTexture(image.getGlTextureId());
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // 启用多重采样
        glEnable(GL13.GL_MULTISAMPLE);

        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, imgWidth, imgHeight, imgWidth, imgHeight);

        // 禁用多重采样
        glDisable(GL13.GL_MULTISAMPLE);

        GLUtil.endBlend();
    }

    public static void drawImage(BufferedImage image, float x, float y, float imgWidth, float imgHeight) {
        uploadTexture(image);
        drawImage(bufferedImageMap.get(image), x, y, imgWidth, imgHeight);
    }

    public static void drawImage(ResourceLocation resourceLocation, float x, float y, float imgWidth, float imgHeight, Color color) {
        color(color.getRGB());
        drawImage(resourceLocation, x, y, imgWidth, imgHeight);
        resetColor();
    }

    /**
     * Sometimes colors get messed up in for loops, so we use this method to reset it to allow new colors to be used
     */
    public static void resetColor() {
        GlStateManager.color(1, 1, 1, 1);
    }

    /**
     * Scales the data that you put in the runnable
     *
     * @param x     start x pos
     * @param y     start y pos
     * @param scale scale
     */
    public static void scaleStart(float x, float y, float scale) {
        glPushMatrix();
        glTranslatef(x, y, 0);
        glScalef(scale, scale, 1);
        glTranslatef(-x, -y, 0);
    }

    /**
     * End scale
     */
    public static void scaleEnd() {
        glPopMatrix();
    }

    /**
     * GL Scissor
     *
     * @param x      x
     * @param y      y
     * @param width  width
     * @param height height
     */
    public static void scissorStart(double x, double y, double width, double height) {
        glPushMatrix();
        glEnable(GL_SCISSOR_TEST);
        ScaledResolution sr = new ScaledResolution(mc);
        double scale = sr.getScaleFactor();
        double finalHeight = height * scale;
        double finalY = (sr.getScaledHeight() - y) * scale;
        double finalX = x * scale;
        double finalWidth = width * scale;
        glScissor((int) finalX, (int) (finalY - finalHeight), (int) finalWidth, (int) finalHeight);
    }

    public static void scissorStart(double x, double y, double width, double height, double centerX, double centerY, double scale) {
        glPushMatrix();
        glEnable(GL_SCISSOR_TEST);

        // 获取Minecraft的分辨率信息
        ScaledResolution sr = new ScaledResolution(mc);
        double resolutionScale = sr.getScaleFactor();

        // 计算基于缩放的最终剪裁范围
        double finalWidth = width * scale * resolutionScale;
        double finalHeight = height * scale * resolutionScale;

        // 调整剪裁矩形的左下角坐标
        double adjustedX = (x - centerX) * scale + centerX;
        double adjustedY = (y - centerY) * scale + centerY;

        // 将调整后的坐标转换为屏幕坐标
        double finalX = adjustedX * resolutionScale;
        double finalY = (sr.getScaledHeight() - adjustedY) * resolutionScale;

        // 调整 glScissor 的参数
        glScissor((int) finalX, (int) (finalY - finalHeight), (int) finalWidth, (int) finalHeight);
    }


    /**
     * End GL Scissor
     */
    public static void scissorEnd() {
        glDisable(GL_SCISSOR_TEST);
        glPopMatrix();
    }

    /**
     * Judge if cursor is hovering specific area.
     *
     * @param mouseX mX
     * @param mouseY mY
     * @param x      x
     * @param y      y
     * @param width  width
     * @param height height
     * @return boolean
     */
    public static boolean hovering(float mouseX, float mouseY, float x, float y, float width, float height) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    /**
     * Judge if mouse button is down while the cursor in specific area.
     *
     * @param mouseX mX
     * @param mouseY mY
     * @param x      x
     * @param y      y
     * @param width  width
     * @param height height
     * @param button mouse button
     * @return boolean
     */
    public static boolean holding(float mouseX, float mouseY, float x, float y, float width, float height, int button) {
        return Mouse.isButtonDown(button) && hovering(mouseX, mouseY, x, y, width, height);
    }

    /**
     * This method colors the next available texture with a specified alpha value ranging from 0-1
     */
    public static void color(int color, float alpha) {
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        GlStateManager.color(r, g, b, alpha);
    }

    /**
     * Colors the next texture without a specified alpha value
     *
     * @param color color
     */
    public static void color(int color) {
        color(color, (float) (color >> 24 & 255) / 255.0F);
    }
    public static void drawTexturedRect(int texture, float x, float y, float width, float height) {
        GLUtil.startBlend();

        // 绑定纹理并设置过滤参数
        GlStateManager.bindTexture(texture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // 启用多重采样
        glEnable(GL13.GL_MULTISAMPLE);

        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);

        // 禁用多重采样
        glDisable(GL13.GL_MULTISAMPLE);

        GLUtil.endBlend();
    }
}
