package cn.feng.aluminium.ui.music.api.bean;

import cn.feng.aluminium.ui.music.api.bean.lyric.Lyric;
import cn.feng.aluminium.ui.music.api.bean.lyric.LyricLine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class Music {
    // Metadata
    private final long id;

    // Display information
    private final String title;
    private final String artist;
    private final Album album;
    private final int duration;

    // Lyric
    private Lyric lyric;

    public Music(long id, String title, String artist, Album album, int duration) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
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

    public Album getAlbum() {
        return album;
    }

    public int getDuration() {
        return duration;
    }

    public void setLyric(Lyric lyric) {
        this.lyric = lyric;
    }

    public Lyric getLyric() {
        return lyric;
    }
}
