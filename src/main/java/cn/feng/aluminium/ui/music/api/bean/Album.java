package cn.feng.aluminium.ui.music.api.bean;

import java.awt.image.BufferedImage;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class Album {
    // Metadata
    private final long id;

    // Display information
    private final String title;
    private Cover cover;

    // Content
    private Playlist playlist;

    public Album(long id, String title, Cover cover) {
        this.id = id;
        this.title = title;
        this.cover = cover;
    }

    public Album(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Cover getCover() {
        return cover;
    }

    public void setCover(Cover cover) {
        this.cover = cover;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }
}
