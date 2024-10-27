package cn.feng.aluminium.ui.music;

import cn.feng.aluminium.ui.music.api.bean.Album;
import cn.feng.aluminium.ui.music.api.bean.Music;
import cn.feng.aluminium.ui.music.api.bean.Playlist;
import cn.feng.aluminium.ui.music.api.bean.User;
import cn.feng.aluminium.ui.music.api.player.MusicPlayer;
import cn.feng.aluminium.ui.music.gui.MusicScreen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class MusicManager {
    public long likeId;
    private final Map<Long, Music> musicMap = new HashMap<>();
    private final Map<Long, Album> albumMap = new HashMap<>();
    private final Map<Long, Playlist> playlistMap = new HashMap<>();
    private MusicScreen screen;
    private MusicPlayer player;
    private User user = new User();

    public void init() {
        screen = new MusicScreen();
        player = new MusicPlayer();
    }

    public MusicPlayer getPlayer() {
        return player;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Map<Long, Music> getMusicMap() {
        return musicMap;
    }

    public Map<Long, Album> getAlbumMap() {
        return albumMap;
    }

    public Map<Long, Playlist> getPlaylistMap() {
        return playlistMap;
    }

    public MusicScreen getScreen() {
        return screen;
    }

    public void setScreen(MusicScreen screen) {
        this.screen = screen;
    }
}
