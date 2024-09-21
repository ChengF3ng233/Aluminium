package cn.feng.aluminium.ui.music.ui.component.impl;

import cn.feng.aluminium.ui.music.ui.Theme;
import cn.feng.aluminium.ui.music.ui.component.AbstractComponent;
import cn.feng.aluminium.util.animation.advanced.Animation;
import cn.feng.aluminium.util.animation.advanced.Direction;
import cn.feng.aluminium.util.animation.advanced.composed.ColorAnimation;
import cn.feng.aluminium.util.animation.advanced.impl.EaseOutCubic;
import cn.feng.aluminium.util.render.RenderUtil;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class IconButtonComponent extends AbstractComponent {

    protected ResourceLocation iconLocation;
    private final ColorAnimation iconColor;
    private final Animation scaleAnimation;
    private Runnable action;
    private boolean selectable;
    private boolean selected;

    public void setAction(Runnable action) {
        this.action = action;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public ResourceLocation getIconLocation() {
        return iconLocation;
    }

    public IconButtonComponent(ResourceLocation iconLocation, Runnable action) {
        this.iconLocation = iconLocation;
        this.action = action;
        iconColor = new ColorAnimation(Theme.grey, Theme.grey, 300);
        scaleAnimation = new EaseOutCubic(150, 0.1, Direction.BACKWARDS);
        selectable = false;
    }

    public IconButtonComponent(ResourceLocation iconLocation, Runnable action, boolean selectable) {
        this(iconLocation, action);
        this.selectable = selectable;
    }

    @Override
    public void render() {
        Color targetColor = (hovering || (selected && selectable)) ? ((holding || (selected && selectable)) ? Theme.secondary : new Color(200, 200, 200, 200)) : Theme.grey;
        iconColor.change(targetColor);

        if (scaleAnimation.finished(Direction.FORWARDS) && !holding) scaleAnimation.changeDirection();

        RenderUtil.scaleStart(renderX + width / 2f, renderY + height / 2f, 1f - scaleAnimation.getOutput().floatValue());
        RenderUtil.drawImage(iconLocation, renderX + 2f, renderY + 2f, width - 4f, height - 4f, iconColor.getOutput());
        RenderUtil.scaleEnd();
    }

    @Override
    public void mouseClicked(int mouseButton) {
        if (hovering) {
            scaleAnimation.setDirection(Direction.FORWARDS);
            scaleAnimation.reset();
            if (selectable && !selected) {
                selected = true;
            }
            action.run();
        }
    }
}
