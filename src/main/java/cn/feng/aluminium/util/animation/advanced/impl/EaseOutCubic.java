package cn.feng.aluminium.util.animation.advanced.impl;

import cn.feng.aluminium.util.animation.advanced.Animation;
import cn.feng.aluminium.util.animation.advanced.Direction;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class EaseOutCubic extends Animation {
    public EaseOutCubic(int ms, double endPoint) {
        super(ms, endPoint);
    }

    public EaseOutCubic(int ms, double endPoint, Direction direction) {
        super(ms, endPoint, direction);
    }

    @Override
    protected double getEquation(double x) {
        return 1 - Math.pow(1 - x, 4);
    }
}
