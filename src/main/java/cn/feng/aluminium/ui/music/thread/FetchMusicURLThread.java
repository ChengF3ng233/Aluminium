package cn.feng.aluminium.ui.music.thread;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.music.api.MusicApi;
import cn.feng.aluminium.ui.music.api.bean.Music;
import cn.feng.aluminium.ui.music.api.bean.lyric.Lyric;

/**
 * @author ChengFeng
 * @since 2024/9/17
 **/
public class FetchMusicURLThread extends Thread {
    private final Music music;

    public FetchMusicURLThread(Music music) {
        this.music = music;
    }

    @Override
    public void run() {
        String url = MusicApi.getSongUrl(music.getId());
        Lyric lyric = MusicApi.getLyric(music);
        music.setLyric(lyric);
        Aluminium.INSTANCE.musicManager.getPlayer().play(url);
    }
}
