package cn.feng.aluminium.value.impl;

import cn.feng.aluminium.value.Value;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class NumberValue extends Value<Double> {
    public double maximum;
    public double minimum;
    public double step;

    public NumberValue(String name, double value, double maximum, double minimum, double step) {
        super(name, value);
        this.maximum = maximum;
        this.minimum = minimum;
        this.step = step;
    }
}
