package cn.feng.aluminium.ui.music.api.bean;

import net.minecraft.client.renderer.texture.DynamicTexture;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class Album {
    // Metadata
    private final long id;

    // Display information
    private final String title;
    private final String artist;
    private final String coverUrl;
    private DynamicTexture coverTexture;

    // Content
    private final Playlist playlist;

    public Album(long id, String title, String artist, String coverUrl, DynamicTexture coverTexture, Playlist playlist) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.coverUrl = coverUrl;
        this.coverTexture = coverTexture;
        this.playlist = playlist;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public DynamicTexture getCoverTexture() {
        return coverTexture;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setCoverTexture(DynamicTexture coverTexture) {
        this.coverTexture = coverTexture;
    }
}
