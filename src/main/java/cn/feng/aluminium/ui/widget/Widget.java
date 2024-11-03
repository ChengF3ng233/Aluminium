package cn.feng.aluminium.ui.widget;

import cn.feng.aluminium.ui.font.FontManager;
import cn.feng.aluminium.util.Util;
import cn.feng.aluminium.util.render.ColorUtil;
import cn.feng.aluminium.util.render.RenderUtil;
import cn.feng.aluminium.util.render.shader.ShaderUtil;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public abstract class Widget extends Util {
    public String name;
    /**
     * x, y都使用相对位置 （百分比），防止因窗口缩放导致组件乱动
     */
    public float x, y;
    protected float renderX, renderY;
    public float width, height;
    public final boolean defaultOn;
    public boolean dragging;
    private int dragX, dragY;
    private int align;

    protected ScaledResolution sr;

    public Widget(String name, boolean defaultOn) {
        this.name = name;
        this.defaultOn = defaultOn;
        this.x = 0f;
        this.y = 0f;
        this.width = 100f;
        this.height = 100f;
        this.align = WidgetAlign.LEFT | WidgetAlign.TOP;
    }

    public Widget(String name, boolean defaultOn, int align) {
        this(name, defaultOn);
        this.align = align;
    }

    public abstract void render();

    public void update() {
        sr = new ScaledResolution(mc);

        renderX = x * sr.getScaledWidth();
        renderY = y * sr.getScaledHeight();

        if ((align & WidgetAlign.RIGHT) != 0) {
            renderX -= width;
        } else if ((align & WidgetAlign.CENTER) != 0) {
            renderX -= width / 2f;
        }

        if ((align & WidgetAlign.BOTTOM) != 0) {
            renderY -= height;
        } else if ((align & WidgetAlign.MIDDLE) != 0) {
            renderY -= height / 2f;
        }

        if (renderX < 0f) renderX = 0f;
        if (renderX > sr.getScaledWidth() - width) renderX = sr.getScaledWidth() - width;
        if (renderY < 0f) renderY = 0f;
        if (renderY > sr.getScaledHeight() - height) renderY = sr.getScaledHeight() - height;

        float minX = 0f;
        float maxX = 1f;
        float minY = 0f;
        float maxY = 1f;

        if ((align & WidgetAlign.RIGHT) != 0) {
            minX += width / sr.getScaledWidth();
            maxX -= width / sr.getScaledWidth();
        } else if ((align & WidgetAlign.CENTER) != 0) {
            minX += (width / 2f) / sr.getScaledWidth();
            maxX -= (width / 2f) / sr.getScaledWidth();
        }

        if ((align & WidgetAlign.TOP) != 0) {
            maxY -= height / sr.getScaledHeight();
        } else if ((align & WidgetAlign.MIDDLE) != 0) {
            minY += (height / 2f) / sr.getScaledHeight();
            maxY -= (height / 2f) / sr.getScaledHeight();
        } else if ((align & WidgetAlign.BOTTOM) != 0) {
            minY += height / sr.getScaledHeight();
        }

        x = Math.max(x, minX);
        x = Math.min(x, maxX);
        y = Math.max(y, minY);
        y = Math.min(y, maxY);
    }

    public final void onChatGUI(int mouseX, int mouseY, boolean drag) {
        boolean hovering = RenderUtil.hovering(mouseX, mouseY, renderX, renderY, width, height);

        if (hovering || dragging) {
            FontManager.poppins(16).drawString(name, renderX, renderY - FontManager.poppins(16).getHeight() - 3f, Color.WHITE.getRGB(), true);
        }

        if (dragging) {
            ShaderUtil.drawRoundOutline(renderX, renderY, width, height, 2f, 0.05f, ColorUtil.TRANSPARENT_COLOR, Color.WHITE);
        }

        if (hovering && Mouse.isButtonDown(0) && !dragging && drag) {
            dragging = true;
            dragX = mouseX;
            dragY = mouseY;
        }

        if (!Mouse.isButtonDown(0)) dragging = false;

        if (dragging) {
            float deltaX = (float) (mouseX - dragX) / sr.getScaledWidth();
            float deltaY = (float) (mouseY - dragY) / sr.getScaledHeight();

            x += deltaX;
            y += deltaY;

            dragX = mouseX;
            dragY = mouseY;
        }
    }
}
