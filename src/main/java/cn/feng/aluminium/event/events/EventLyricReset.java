package cn.feng.aluminium.event.events;

import cn.feng.aluminium.event.impl.Event;
import cn.feng.aluminium.ui.music.api.bean.Music;

public class EventLyricReset implements Event {
    private final Music music;
    private final float time;

    public EventLyricReset(Music music, float time) {
        this.music = music;
        this.time = time;
    }

    public Music getMusic() {
        return music;
    }

    public float getTime() {
        return time;
    }
}
