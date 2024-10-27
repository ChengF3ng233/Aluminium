package cn.feng.aluminium.ui.music.thread;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.music.api.MusicApi;
import cn.feng.aluminium.ui.music.api.bean.Playlist;
import cn.feng.aluminium.ui.music.gui.page.impl.PlaylistPage;

public class SearchMusicThread extends Thread {
    private final String keyword;

    public SearchMusicThread(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void run() {
        Playlist search = MusicApi.search(keyword);
        Aluminium.INSTANCE.musicManager.getScreen().changePage(new PlaylistPage(search), false);
    }
}
