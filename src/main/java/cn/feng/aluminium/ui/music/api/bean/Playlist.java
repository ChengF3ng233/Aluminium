package cn.feng.aluminium.ui.music.api.bean;

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
    // Content
    private final List<Music> musicList = new ArrayList<>();
    private String description;
    private String author;
    private Cover cover;
    private boolean completelyDownloaded;

    public Playlist(long id, String title, String description, String author, Cover cover) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.cover = cover;
    }

    public Playlist(long id, String title, String description, String author, Cover cover, List<Music> musicList) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.cover = cover;
        this.musicList.addAll(musicList);
    }

    public Playlist(long id, String title, Cover cover) {
        this.id = id;
        this.title = title;
        this.cover = cover;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Cover getCover() {
        return cover;
    }

    public void setCover(Cover cover) {
        this.cover = cover;
    }

    public List<Music> getMusicList() {
        return musicList;
    }

    public boolean isCompletelyDownloaded() {
        return completelyDownloaded;
    }

    public void setCompletelyDownloaded(boolean completelyDownloaded) {
        this.completelyDownloaded = completelyDownloaded;
    }
}
