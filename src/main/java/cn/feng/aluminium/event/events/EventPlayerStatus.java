package cn.feng.aluminium.event.events;

import cn.feng.aluminium.event.impl.Event;
import javafx.scene.media.MediaPlayer;

public class EventPlayerStatus implements Event {
    private final MediaPlayer.Status status;

    public EventPlayerStatus(MediaPlayer.Status status) {
        this.status = status;
    }

    public MediaPlayer.Status getStatus() {
        return status;
    }
}
