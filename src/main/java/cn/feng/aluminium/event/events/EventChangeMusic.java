package cn.feng.aluminium.event.events;

import cn.feng.aluminium.event.impl.Event;
import cn.feng.aluminium.ui.music.api.bean.Music;

public class EventChangeMusic implements Event {
    private final Music music;

    public EventChangeMusic(Music music) {
        this.music = music;
    }

    public Music getMusic() {
        return music;
    }
}
