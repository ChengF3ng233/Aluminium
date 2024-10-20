package cn.feng.aluminium.ui.music.api.bean;

import java.awt.image.BufferedImage;
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
    private BufferedImage coverImage;

    // Content
    private final List<Music> musicList = new ArrayList<>();
    private boolean completelyDownloaded;

    public Playlist(long id, String title, String description, String author, String coverUrl, BufferedImage coverImage) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.coverUrl = coverUrl;
        this.coverImage = coverImage;
    }
    public Playlist(long id, String title, String description, String author, String coverUrl, BufferedImage coverImage, List<Music> musicList) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.coverUrl = coverUrl;
        this.coverImage = coverImage;
        this.musicList.addAll(musicList);
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

    public BufferedImage getCoverImage() {
        return coverImage;
    }

    public List<Music> getMusicList() {
        return musicList;
    }

    public void setCoverImage(BufferedImage coverImage) {
        this.coverImage = coverImage;
    }

    public boolean isCompletelyDownloaded() {
        return completelyDownloaded;
    }

    public void setCompletelyDownloaded(boolean completelyDownloaded) {
        this.completelyDownloaded = completelyDownloaded;
    }
}
