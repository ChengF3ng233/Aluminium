package cn.feng.aluminium.ui.music.thread;

import cn.feng.aluminium.ui.music.api.MusicApi;
import cn.feng.aluminium.ui.music.api.bean.Music;
import cn.feng.aluminium.ui.music.api.bean.Playlist;
import cn.feng.aluminium.ui.music.gui.component.impl.composed.PlaylistComponent;

import java.io.IOException;
import java.util.List;

public class FetchPlaylistMusicThread extends Thread {
    private final PlaylistComponent component;
    private final Playlist playlist;
    private final PlaylistType type;
    private List<Music> result = null;

    public FetchPlaylistMusicThread(PlaylistComponent component, Playlist playlist, PlaylistType type) {
        this.component = component;
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
                    result = MusicApi.loadCloudMusic(playlist);
                    break;
                }

                case LIKED:
                case SEARCH:
                case NORMAL: {
                    result = MusicApi.loadPlaylist(playlist);
                }
            }
            result.removeIf(it -> playlist.getMusicList().contains(it));
            playlist.getMusicList().addAll(result);
            component.updateButtons();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
