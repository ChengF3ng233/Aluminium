package cn.feng.aluminium.ui.music;

import cn.feng.aluminium.ui.music.api.bean.Music;
import cn.feng.aluminium.ui.music.api.bean.User;
import cn.feng.aluminium.ui.music.ui.MusicScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class MusicManager {
    private MusicScreen screen;
    private User user = new User();

    private final List<Music> musicList = new ArrayList<>();

    public void init() {
        screen = new MusicScreen();
        if (user.getAvatarUrl() != null) {

        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setScreen(MusicScreen screen) {
        this.screen = screen;
    }

    public List<Music> getMusicList() {
        return musicList;
    }

    public MusicScreen getScreen() {
        return screen;
    }
}
