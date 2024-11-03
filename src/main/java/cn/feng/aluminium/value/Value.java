package cn.feng.aluminium.value;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class Value<T> {
    private final String name;
    protected T value;
    private BiConsumer<T, T> onChange;

    public Value(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public Value(String name, T value, BiConsumer<T, T> onChange) {
        this.name = name;
        this.value = value;
        this.onChange = onChange;
    }

    public void setValue(T value) {
        if (Objects.equals(this.value, value)) return;
        onChange.accept(this.value, value);
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public void setOnChange(BiConsumer<T, T> onChange) {
        this.onChange = onChange;
    }
}
