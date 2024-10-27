package cn.feng.aluminium.ui.music.gui.page.impl;

import cn.feng.aluminium.Aluminium;

public class LikePage extends PlaylistPage {
    public void load() {
        setPlaylist(Aluminium.INSTANCE.musicManager.getPlaylistMap().get(Aluminium.INSTANCE.musicManager.likeId));
    }
}
