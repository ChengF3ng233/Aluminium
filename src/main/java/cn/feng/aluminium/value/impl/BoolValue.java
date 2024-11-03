package cn.feng.aluminium.value.impl;

import cn.feng.aluminium.value.Value;

import java.util.function.BiConsumer;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class BoolValue extends Value<Boolean> {
    public BoolValue(String name, Boolean value) {
        super(name, value);
    }

    public BoolValue(String name, Boolean value, BiConsumer<Boolean, Boolean> onChange) {
        super(name, value, onChange);
    }
}
