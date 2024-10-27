package cn.feng.aluminium.util.render.shader;

import cn.feng.aluminium.util.render.ColorUtil;
import cn.feng.aluminium.util.render.GLUtil;
import cn.feng.aluminium.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class ShaderUtil {
    private static final Shader roundedShader = new Shader("roundedRect");
    private static final Shader roundedVaryingShader = new Shader("roundedVaryingRect");
    private static final Shader roundedOutlineShader = new Shader("roundRectOutline");
    private static final Shader roundedTexturedShader = new Shader("roundRectTexture");
    private static final Shader roundedGradientShader = new Shader("roundedRectGradient");
    private static final Shader circleShader = new Shader("circle");

    public static void drawGradientHorizontal(float x, float y, float width, float height, float radius, Color left, Color right) {
        drawGradientRound(x, y, width, height, radius, left, left, right, right);
    }

    public static void drawGradientVertical(float x, float y, float width, float height, float radius, Color top, Color bottom) {
        drawGradientRound(x, y, width, height, radius, bottom, top, bottom, top);
    }

    public static void drawGradientCornerLR(float x, float y, float width, float height, float radius, Color topLeft, Color bottomRight) {
        Color mixedColor = ColorUtil.interpolateColorC(topLeft, bottomRight, .5f);
        drawGradientRound(x, y, width, height, radius, mixedColor, topLeft, bottomRight, mixedColor);
    }

    public static void drawGradientCornerRL(float x, float y, float width, float height, float radius, Color bottomLeft, Color topRight) {
        Color mixedColor = ColorUtil.interpolateColorC(topRight, bottomLeft, .5f);
        drawGradientRound(x, y, width, height, radius, bottomLeft, mixedColor, mixedColor, topRight);
    }

    public static void drawGradientRound(float x, float y, float width, float height, float radius, Color bottomLeft, Color topLeft, Color bottomRight, Color topRight) {
        RenderUtil.setAlphaLimit(0);
        RenderUtil.resetColor();
        GLUtil.startBlend();
        roundedGradientShader.init();
        setupRoundedRectUniforms(x, y, width, height, radius, roundedGradientShader);
        //Top left
        roundedGradientShader.setUniformf("color1", topLeft.getRed() / 255f, topLeft.getGreen() / 255f, topLeft.getBlue() / 255f, topLeft.getAlpha() / 255f);
        // Bottom Left
        roundedGradientShader.setUniformf("color2", bottomLeft.getRed() / 255f, bottomLeft.getGreen() / 255f, bottomLeft.getBlue() / 255f, bottomLeft.getAlpha() / 255f);
        //Top Right
        roundedGradientShader.setUniformf("color3", topRight.getRed() / 255f, topRight.getGreen() / 255f, topRight.getBlue() / 255f, topRight.getAlpha() / 255f);
        //Bottom Right
        roundedGradientShader.setUniformf("color4", bottomRight.getRed() / 255f, bottomRight.getGreen() / 255f, bottomRight.getBlue() / 255f, bottomRight.getAlpha() / 255f);
        Shader.drawQuads(x, y, width, height);
        roundedGradientShader.unload();
        GLUtil.endBlend();
    }

    public static void drawRound(float x, float y, float width, float height, float radius, Color color) {
        RenderUtil.resetColor();
        GLUtil.startBlend();
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        RenderUtil.setAlphaLimit(0);
        roundedShader.init();

        setupRoundedRectUniforms(x, y, width, height, radius, roundedShader);
        roundedShader.setUniformf("color", color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);

        Shader.drawQuads(x, y, width, height);
        roundedShader.unload();
        GLUtil.endBlend();
    }

    public static void drawCircle(float centerX, float centerY, float radius, Color color) {
        drawRound(centerX - radius, centerY - radius, radius * 2f, radius * 2f, radius, color);
/*        RenderUtil.resetColor();
        GLUtil.startBlend();
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        RenderUtil.setAlphaLimit(0);
        circleShader.init();

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        circleShader.setUniformf("center", centerX * sr.getScaleFactor(), centerY * sr.getScaleFactor());
        circleShader.setUniformf("radius", radius * sr.getScaleFactor());
        circleShader.setUniformf("color", color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);

        Shader.drawQuads(centerX - radius, centerY - radius, radius * 2f, radius * 2f);
        circleShader.unload();
        GLUtil.endBlend();*/
    }

    public static void drawVaryingRound(float x, float y, float width, float height, float radiusTL, float radiusTR, float radiusBL, float radiusBR, Color color) {
        RenderUtil.resetColor();
        GLUtil.startBlend();
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        RenderUtil.setAlphaLimit(0);
        roundedVaryingShader.init();

        setupRoundedVaryingRectUniforms(x, y, width, height, radiusTL, radiusTR, radiusBL, radiusBR, roundedVaryingShader);
        roundedVaryingShader.setUniformf("color", color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);

        Shader.drawQuads(x, y, width, height);
        roundedVaryingShader.unload();
        GLUtil.endBlend();
    }

    public static void drawRoundOutline(float x, float y, float width, float height, float radius, float outlineThickness, Color color, Color outlineColor) {
        RenderUtil.resetColor();
        GLUtil.startBlend();
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        RenderUtil.setAlphaLimit(0);
        roundedOutlineShader.init();

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        setupRoundedRectUniforms(x, y, width, height, radius, roundedOutlineShader);
        roundedOutlineShader.setUniformf("outlineThickness", outlineThickness * sr.getScaleFactor());
        roundedOutlineShader.setUniformf("color", color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        roundedOutlineShader.setUniformf("outlineColor", outlineColor.getRed() / 255f, outlineColor.getGreen() / 255f, outlineColor.getBlue() / 255f, outlineColor.getAlpha() / 255f);

        Shader.drawQuads(x - outlineThickness, y - outlineThickness, width + (outlineThickness * 2), height + (outlineThickness * 2));
        roundedOutlineShader.unload();
        GLUtil.endBlend();
    }

    public static void drawRoundTextured(float x, float y, float width, float height, float radius, float alpha) {
        RenderUtil.resetColor();
        RenderUtil.setAlphaLimit(0);
        GLUtil.startBlend();
        roundedTexturedShader.init();
        roundedTexturedShader.setUniformi("textureIn", 0);
        setupRoundedRectUniforms(x, y, width, height, radius, roundedTexturedShader);
        roundedTexturedShader.setUniformf("alpha", alpha);
        Shader.drawQuads(x, y, width, height);
        roundedTexturedShader.unload();
        GLUtil.endBlend();
    }

    private static void setupRoundedRectUniforms(float x, float y, float width, float height, float radius, Shader roundedTexturedShader) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        roundedTexturedShader.setUniformf("location", x * sr.getScaleFactor(),
                (Minecraft.getMinecraft().displayHeight - (height * sr.getScaleFactor())) - (y * sr.getScaleFactor()));
        roundedTexturedShader.setUniformf("rectSize", width * sr.getScaleFactor(), height * sr.getScaleFactor());
        roundedTexturedShader.setUniformf("radius", radius * sr.getScaleFactor());
    }

    private static void setupRoundedVaryingRectUniforms(float x, float y, float width, float height, float radiusTL, float radiusTR, float radiusBL, float radiusBR, Shader roundedTexturedShader) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        roundedTexturedShader.setUniformf("location", x * sr.getScaleFactor(),
                (Minecraft.getMinecraft().displayHeight - (height * sr.getScaleFactor())) - (y * sr.getScaleFactor()));
        roundedTexturedShader.setUniformf("rectSize", width * sr.getScaleFactor(), height * sr.getScaleFactor());
        roundedTexturedShader.setUniformf("radiusTL", radiusTL * sr.getScaleFactor());
        roundedTexturedShader.setUniformf("radiusTR", radiusTR * sr.getScaleFactor());
        roundedTexturedShader.setUniformf("radiusBL", radiusBL * sr.getScaleFactor());
        roundedTexturedShader.setUniformf("radiusBR", radiusBR * sr.getScaleFactor());
    }
}
