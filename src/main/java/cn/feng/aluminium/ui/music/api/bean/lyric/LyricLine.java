package cn.feng.aluminium.ui.music.api.bean.lyric;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class LyricLine {
    private final String line;
    private final int startTime;
    private int duration;

    private final List<LyricChar> charList = new ArrayList<>();

    public LyricLine(String line, int startTime, int duration) {
        this.line = line;
        this.startTime = startTime;
        this.duration = duration;
    }

    public String getLine() {
        return line;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getDuration() {
        return duration;
    }

    public List<LyricChar> getCharList() {
        return charList;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
