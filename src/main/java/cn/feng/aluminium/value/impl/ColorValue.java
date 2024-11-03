package cn.feng.aluminium.value.impl;

import cn.feng.aluminium.value.Value;

import java.awt.*;
import java.util.function.BiConsumer;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class ColorValue extends Value<Color> {
    public ColorValue(String name, Color value) {
        super(name, value);
    }

    public ColorValue(String name, Color value, BiConsumer<Color, Color> onChange) {
        super(name, value, onChange);
    }
}
