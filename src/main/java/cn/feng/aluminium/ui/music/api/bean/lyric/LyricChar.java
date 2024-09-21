package cn.feng.aluminium.ui.music.api.bean.lyric;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class LyricChar {
    private final String character;
    private final int startTime;
    private int duration;

    public LyricChar(String character, int startTime, int duration) {
        this.character = character;
        this.startTime = startTime;
        this.duration = duration;
    }

    public String getCharacter() {
        return character;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}