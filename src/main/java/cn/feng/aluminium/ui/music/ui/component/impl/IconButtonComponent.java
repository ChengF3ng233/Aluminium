package cn.feng.aluminium.ui.music.ui.component.impl;

import cn.feng.aluminium.ui.music.ui.component.AbstractComponent;
import cn.feng.aluminium.util.animation.advanced.Animation;
import cn.feng.aluminium.util.animation.advanced.Direction;
import cn.feng.aluminium.util.animation.advanced.composed.ColorAnimation;
import cn.feng.aluminium.util.animation.advanced.impl.EaseOutCubic;
import cn.feng.aluminium.util.render.ColorUtil;
import cn.feng.aluminium.util.render.RenderUtil;
import cn.feng.aluminium.util.render.shader.ShaderUtil;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class IconButtonComponent extends AbstractComponent {

    private final ResourceLocation iconLocation;
    private final ColorAnimation backgroundColor;
    private final Animation scaleAnimation;
    private final Runnable action;

    public IconButtonComponent(ResourceLocation iconLocation, Runnable action) {
        this.iconLocation = iconLocation;
        this.action = action;
        backgroundColor = new ColorAnimation(ColorUtil.TRANSPARENT_COLOR, ColorUtil.TRANSPARENT_COLOR, 300);
        scaleAnimation = new EaseOutCubic(300, 0.2, Direction.BACKWARDS);
    }

    @Override
    public void render() {
        Color targetColor = hovering ? (holding ? new Color(255, 255, 255, 120) : new Color(200, 200, 200, 100)) : ColorUtil.TRANSPARENT_COLOR;

        if (!backgroundColor.getEndColor().equals(targetColor)) {
            backgroundColor.setEndColor(targetColor);
            backgroundColor.reset();
        }

        if (scaleAnimation.finished(Direction.FORWARDS)) scaleAnimation.changeDirection();

        RenderUtil.scaleStart(renderX + width / 2f, renderY + height / 2f, 1f - scaleAnimation.getOutput().floatValue());
        ShaderUtil.drawCircle(renderX + width / 2f, renderY + height / 2f, width, backgroundColor.getOutput());
        RenderUtil.drawImage(iconLocation, renderX + 2f, renderY + 2f, width - 4f, height - 4f);
        RenderUtil.scaleEnd();
    }

    @Override
    public void onMouseClicked(int mouseButton) {
        if (hovering) {
            scaleAnimation.setDirection(Direction.FORWARDS);
            scaleAnimation.reset();
            action.run();
        }
    }
}
