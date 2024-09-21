package cn.feng.aluminium.ui.music.ui.component.impl;

import cn.feng.aluminium.ui.music.api.bean.Playlist;
import cn.feng.aluminium.ui.music.ui.component.AbstractComponent;

/**
 * @author ChengFeng
 * @since 2024/9/17
 **/
public class PlaylistButtonComponent extends AbstractComponent {
    private Playlist playlist;

    public PlaylistButtonComponent(Playlist playlist) {
        this.playlist = playlist;
    }

    @Override
    public void render() {

    }
}
