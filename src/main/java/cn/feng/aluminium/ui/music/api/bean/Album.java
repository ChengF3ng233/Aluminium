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
    private final String coverUrl;
    private final BufferedImage coverImage;

    // Content
    private Playlist playlist;

    public Album(long id, String title, String coverUrl, BufferedImage coverImage) {
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.coverImage = coverImage;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public BufferedImage getCoverImage() {
        return coverImage;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }
}
