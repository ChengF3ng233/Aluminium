package cn.feng.aluminium.ui.music.thread;

import cn.feng.aluminium.ui.music.api.MusicApi;
import cn.feng.aluminium.ui.music.api.bean.Playlist;
import cn.feng.aluminium.ui.music.gui.page.Pages;

import java.io.IOException;

public class FetchPlaylistThread extends Thread {
    public FetchPlaylistThread() {
        setName("Music-FetchRecommendation");
    }

    @Override
    public void run() {
        try {
            Pages.recommendPage.addPlaylist(MusicApi.getDailySongs());
            for (Playlist recommendedPlayList : MusicApi.getRecommendedPlayLists()) {
                Pages.recommendPage.addPlaylist(recommendedPlayList);
            }
            Pages.cloudPage.setPlaylist(MusicApi.getCloudMusics());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
