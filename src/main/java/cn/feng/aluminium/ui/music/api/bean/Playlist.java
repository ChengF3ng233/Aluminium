package cn.feng.aluminium.ui.music.api.bean;

import net.minecraft.client.renderer.texture.DynamicTexture;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class Playlist {
    // Metadata
    private final long id;

    // Display information
    private final String title;
    private final String description;
    private final String author;
    private final String coverUrl;
    private DynamicTexture coverTexture;

    // Content
    private final List<Music> musicList = new ArrayList<>();

    public Playlist(long id, String title, String description, String coverUrl, String author) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.coverUrl = coverUrl;
        this.author = author;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public DynamicTexture getCoverTexture() {
        return coverTexture;
    }

    public List<Music> getMusicList() {
        return musicList;
    }
}
