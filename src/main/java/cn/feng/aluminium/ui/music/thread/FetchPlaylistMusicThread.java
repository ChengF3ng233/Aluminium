package cn.feng.aluminium.ui.music.thread;

import cn.feng.aluminium.ui.music.api.MusicApi;
import cn.feng.aluminium.ui.music.api.bean.Music;
import cn.feng.aluminium.ui.music.api.bean.Playlist;
import cn.feng.aluminium.ui.music.gui.page.impl.PlaylistPage;

import java.io.IOException;
import java.util.List;

public class FetchPlaylistMusicThread extends Thread {
    private final PlaylistPage page;
    private final Playlist playlist;
    private final PlaylistType type;
    private List<Music> result = null;

    public FetchPlaylistMusicThread(PlaylistPage page, Playlist playlist, PlaylistType type) {
        this.page = page;
        this.playlist = playlist;
        this.type = type;
    }

    public List<Music> getResult() {
        return result;
    }

    @Override
    public void run() {
        try {
            switch (type) {
                case CLOUD: {
                    result = MusicApi.fetchCloudMusics(playlist);
                    break;
                }

                case LIKED:
                case SEARCH:
                case NORMAL: {
                    result = MusicApi.fetchMusicList(playlist);
                }
            }
            result.removeIf(it -> playlist.getMusicList().contains(it));
            playlist.getMusicList().addAll(result);
            page.updateButtons();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
