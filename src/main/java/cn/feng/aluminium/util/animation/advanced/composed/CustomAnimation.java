package cn.feng.aluminium.util.animation.advanced.composed;

import cn.feng.aluminium.util.animation.advanced.Animation;
import cn.feng.aluminium.util.animation.advanced.ComposedAnimation;
import cn.feng.aluminium.util.animation.advanced.Direction;
import cn.feng.aluminium.util.animation.advanced.impl.EaseBackIn;

import java.lang.reflect.InvocationTargetException;

/**
 * @author ChengFeng
 * @since 2024/7/29
 **/
public class CustomAnimation extends ComposedAnimation<Double> {
    private Animation animation;
    private double startPoint;
    private double endPoint;
    private int duration;

    public CustomAnimation(Class<? extends Animation> animationClass, int ms, double startPoint, double endPoint) {
        this.createAnimation(animationClass, ms, startPoint, endPoint, Direction.FORWARDS);
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.duration = ms;
    }

    public CustomAnimation(Class<? extends Animation> animationClass, int ms, double startPoint, double endPoint, Direction direction) {
        this.createAnimation(animationClass, ms, startPoint, endPoint, direction);
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.duration = ms;
    }

    public CustomAnimation(Class<? extends Animation> animationClass, int duration) {
        this.createAnimation(animationClass, duration, startPoint, endPoint, Direction.FORWARDS);
        this.duration = duration;
    }

    public void setStartPoint(double startPoint) {
        this.animation.reset();
        this.startPoint = startPoint;
        this.animation.setEndPoint(this.endPoint - this.startPoint);
    }

    public void setEndPoint(double endPoint) {
        if (this.endPoint == endPoint) return;
        this.endPoint = endPoint;
        this.animation.setEndPoint(this.endPoint - this.startPoint);
    }

    public void setEndPoint(double endPoint, boolean reset) {
        if (this.endPoint == endPoint) return;
        if (reset) {
            startPoint = getOutput();
        }
        setEndPoint(endPoint);
        if (reset) {
            animation.reset();
        }
    }

    public void setDuration(int duration) {
        this.duration = duration;
        this.animation.setDuration(duration);
    }

    public Direction getDirection() {
        return animation.getDirection();
    }

    @Override
    public Double getOutput() {
        return startPoint + animation.getOutput();
    }

    @Override
    public void changeDirection() {
        animation.changeDirection();
    }

    private void createAnimation(Class<? extends Animation> animationClass, int ms, double startPoint, double endPoint, Direction direction) {
        try {
            if (animationClass == EaseBackIn.class) {
                this.animation = animationClass.getConstructor(int.class, double.class, float.class, Direction.class).newInstance(ms, endPoint - startPoint, 2f, direction);
            } else {
                this.animation = animationClass.getConstructor(int.class, double.class, Direction.class).newInstance(ms, endPoint - startPoint, direction);
            }
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Animation getAnimation() {
        return animation;
    }

    public double getStartPoint() {
        return startPoint;
    }

    public double getEndPoint() {
        return endPoint;
    }

    public int getDuration() {
        return duration;
    }
}
