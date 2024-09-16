package cn.feng.aluminium.event.events;

import cn.feng.aluminium.event.impl.Event;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class EventKey implements Event {
    private final int key;

    public EventKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
