package cn.feng.aluminium.value.impl;

import cn.feng.aluminium.util.data.StringUtil;
import cn.feng.aluminium.value.Value;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class ModeValue extends Value<String> {
    public String[] values;

    public ModeValue(String name, String value, String... values) {
        super(name, value);
        this.values = values;
    }

    public void next() {
        int index = StringUtil.arrayIndex(values, getValue()) + 1;
        if (index == values.length) {
            setValue(values[0]);
            return;
        }
        setValue(values[index]);
    }

    public void previous() {
        int index = StringUtil.arrayIndex(values, getValue()) - 1;
        if (index == -1) {
            setValue(values[values.length - 1]);
            return;
        }
        setValue(values[index]);
    }
}
