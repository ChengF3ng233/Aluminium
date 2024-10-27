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
            for (Playlist recommendedPlayList : MusicApi.getPersonalizedPlaylists()) {
                Pages.recommendPage.addPlaylist(recommendedPlayList);
            }
            Pages.cloudPage.setPlaylist(MusicApi.getCloudMusics());
            for (Playlist userPlaylist : MusicApi.getUserPlaylists()) {
                Pages.customPage.addLike(userPlaylist);
            }
            for (Playlist radarPlaylist : MusicApi.getRadarPlaylists()) {
                Pages.customPage.addRadar(radarPlaylist);
            }
            for (Playlist recommendedPlaylist : MusicApi.getRecommendedPlaylists()) {
                Pages.customPage.addRecommend(recommendedPlaylist);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
