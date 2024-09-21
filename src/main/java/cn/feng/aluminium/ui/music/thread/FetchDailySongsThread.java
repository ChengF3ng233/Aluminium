package cn.feng.aluminium.ui.music.thread;

import cn.feng.aluminium.ui.music.api.MusicApi;
import cn.feng.aluminium.ui.music.api.bean.Playlist;

/**
 * @author ChengFeng
 * @since 2024/9/17
 **/
public class FetchDailySongsThread extends Thread {
    private Playlist result = null;

    public FetchDailySongsThread() {
        setName("Music-FetchDailySongs");
    }

    public Playlist getResult() {
        return result;
    }

    @Override
    public void run() {
        result = MusicApi.getDailySongs();
    }
}
