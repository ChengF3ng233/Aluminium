package cn.feng.aluminium.event.events;

import cn.feng.aluminium.event.impl.Event;
import cn.feng.aluminium.ui.music.api.bean.Music;

public class EventResetLyric implements Event {
    private final Music music;
    private final float time;

    public EventResetLyric(Music music, float time) {
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
