package cn.feng.aluminium.ui.music.thread;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.music.api.MusicApi;

/**
 * @author ChengFeng
 * @since 2024/9/17
 **/
public class FetchMusicURLThread extends Thread {
    private final long id;

    public FetchMusicURLThread(long id) {
        this.id = id;
    }

    @Override
    public void run() {
        String url = MusicApi.getSongUrl(id);
        Aluminium.INSTANCE.musicManager.getPlayer().play(url);
    }
}
