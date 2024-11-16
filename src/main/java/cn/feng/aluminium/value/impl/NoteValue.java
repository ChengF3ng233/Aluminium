package cn.feng.aluminium.value.impl;

import cn.feng.aluminium.value.Value;

public class NoteValue extends Value<String> {
    public NoteValue(String value) {
        super(value, value);
    }
}
