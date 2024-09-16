package cn.feng.aluminium.ui.music.api.bean;

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
    private boolean hasTranslate;

    // Display information
    private final String title;
    private final String artist;
    private final Album album;
    private final int duration;

    // Lyric information
    private final List<LyricLine> lyricLines = new ArrayList<>();
    private final List<LyricLine> translateLines = new ArrayList<>();
    private final Map<LyricLine, LyricLine> translateMap = new HashMap<>();

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

    public boolean isHasTranslate() {
        return hasTranslate;
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

    public List<LyricLine> getLyricLines() {
        return lyricLines;
    }

    public List<LyricLine> getTranslateLines() {
        return translateLines;
    }

    public Map<LyricLine, LyricLine> getTranslateMap() {
        return translateMap;
    }

    public void setHasTranslate(boolean hasTranslate) {
        this.hasTranslate = hasTranslate;
    }
}
