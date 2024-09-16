package cn.feng.aluminium.music;

import cn.feng.aluminium.ui.font.SkijaFontLoader;
import cn.feng.aluminium.util.animation.advanced.Animation;
import cn.feng.aluminium.util.animation.advanced.Direction;
import cn.feng.aluminium.util.animation.advanced.composed.ColorAnimation;
import cn.feng.aluminium.util.animation.advanced.impl.EaseBackIn;
import cn.feng.aluminium.util.render.RenderUtil;
import cn.feng.aluminium.util.render.RoundedUtil;
import cn.feng.aluminium.util.render.blur.BlurUtil;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class MusicScreen extends GuiScreen {
    private final float width;
    private final float height;
    private final float topHeight;
    private final float bottomHeight;

    // Position
    private float x, y;
    private Animation windowScale;
    private boolean shouldClose;

    // Drag
    private boolean dragging;
    private float lastMouseX, lastMouseY;

    // Color
    private boolean dark;
    private ColorAnimation backgroundColor;

    public MusicScreen() {
        // Constants
        width = 470f;
        height = 310f;
        topHeight = 30f;
        bottomHeight = 60f;

        // Position
        x = 20f;
        y = 20f;
        windowScale = new EaseBackIn(300, 1.0, 2f);
        shouldClose = true;

        // Color
        dark = true;
        backgroundColor = new ColorAnimation(Theme.dark, Theme.dark, 300);
    }

    @Override
    public void initGui() {
        windowScale.reset();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (windowScale.finished(Direction.BACKWARDS)) {
            mc.displayGuiScreen(null);
            return;
        }

        // Drag logic
        if (RenderUtil.holding(mouseX, mouseY, x, y, width, topHeight, 0)) {
            if (!dragging) {
                lastMouseX = mouseX;
                lastMouseY = mouseY;
                dragging = true;
            }

            x += mouseX - lastMouseX;
            y += mouseY - lastMouseY;

            lastMouseX = mouseX;
            lastMouseY = mouseY;
        } else if (dragging) dragging = false;

        // Render
        RenderUtil.scaleStart(x + width / 2f, y + height / 2f, windowScale.getOutput().floatValue());
        RoundedUtil.drawRound(x, y, width, height, 3f, backgroundColor.getOutput());
        RenderUtil.scaleEnd();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE && shouldClose) windowScale.changeDirection();
    }
}
