package cn.feng.aluminium.util.animation.advanced.composed;

import cn.feng.aluminium.util.animation.advanced.ComposedAnimation;
import cn.feng.aluminium.util.animation.advanced.Direction;
import cn.feng.aluminium.util.animation.advanced.impl.DecelerateAnimation;
import cn.feng.aluminium.util.animation.advanced.impl.EaseOutSine;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/7/29
 **/
public class ColorAnimation extends ComposedAnimation<Color> {
    private Color startColor;
    private Color endColor;
    private int duration;

    private CustomAnimation redAnim;
    private CustomAnimation greenAnim;
    private CustomAnimation blueAnim;
    private CustomAnimation alphaAnim;

    public ColorAnimation(Color startColor, Color endColor, int duration) {
        this.endColor = endColor;
        this.startColor = startColor;
        this.duration = duration;
        createAnim(startColor, endColor, duration);
    }

    public void change(Color newColor) {
        if (endColor.equals(newColor)) return;
        setStartColor(getOutput());
        setEndColor(newColor);
        reset();
    }

    private void createAnim(Color startColor, Color endColor, int duration) {
        this.redAnim = new CustomAnimation(DecelerateAnimation.class, duration, startColor.getRed(), endColor.getRed());
        this.greenAnim = new CustomAnimation(DecelerateAnimation.class, duration, startColor.getGreen(), endColor.getGreen());
        this.blueAnim = new CustomAnimation(DecelerateAnimation.class, duration, startColor.getBlue(), endColor.getBlue());
        this.alphaAnim = new CustomAnimation(DecelerateAnimation.class, duration, startColor.getAlpha(), endColor.getAlpha());
    }

    public void setStartColor(Color startColor) {
        this.startColor = startColor;
        redAnim.setStartPoint(this.startColor.getRed());
        greenAnim.setStartPoint(this.startColor.getGreen());
        blueAnim.setStartPoint(this.startColor.getBlue());
        alphaAnim.setStartPoint(this.startColor.getAlpha());
    }

    public void setEndColor(Color endColor) {
        this.endColor = endColor;
        redAnim.setEndPoint(this.endColor.getRed());
        greenAnim.setEndPoint(this.endColor.getGreen());
        blueAnim.setEndPoint(this.endColor.getBlue());
        alphaAnim.setEndPoint(this.endColor.getAlpha());
    }

    public void setDuration(int duration) {
        this.duration = duration;
        redAnim.setDuration(duration);
        greenAnim.setDuration(duration);
        blueAnim.setDuration(duration);
        alphaAnim.setDuration(duration);
    }

    @Override
    public Color getOutput() {
        return new Color(redAnim.getOutput().intValue(), greenAnim.getOutput().intValue(), blueAnim.getOutput().intValue(), alphaAnim.getOutput().intValue());
    }

    public boolean isFinished() {
        return redAnim.getAnimation().finished(redAnim.getAnimation().getDirection());
    }

    @Override
    public void changeDirection() {
        redAnim.changeDirection();
        greenAnim.changeDirection();
        blueAnim.changeDirection();
        alphaAnim.changeDirection();
    }

    public void reset() {
        redAnim.getAnimation().reset();
        greenAnim.getAnimation().reset();
        blueAnim.getAnimation().reset();
        alphaAnim.getAnimation().reset();
    }

    public Color getStartColor() {
        return startColor;
    }

    public Color getEndColor() {
        return endColor;
    }

    public Direction getDirection() {
        return redAnim.getAnimation().getDirection();
    }
}
