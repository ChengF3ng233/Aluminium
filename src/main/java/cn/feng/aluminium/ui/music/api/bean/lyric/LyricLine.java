package cn.feng.aluminium.ui.music.api.bean.lyric;

import cn.feng.aluminium.util.data.DataUtil;
import cn.feng.aluminium.util.misc.ChatUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class LyricLine {
    private String line;
    private final int startTime;
    private int duration;

    private List<LyricChar> charList = new ArrayList<>();

    public LyricLine(String line, int startTime, int duration) {
        this.line = line;
        this.startTime = startTime;
        this.duration = duration;
    }

    public LyricLine(String line, int startTime, int duration, List<LyricChar> charList) {
        this.line = line;
        this.startTime = startTime;
        this.duration = duration;
        this.charList = charList;
    }

    public LyricChar seek(int time) {
        for (LyricChar lyricChar : charList) {
            if (lyricChar.getStartTime() <= time && lyricChar.getStartTime() + lyricChar.getDuration() >= time) return lyricChar;
        }
        return null;
    }

    public static LyricLine parseLrc(String line) {
        Pattern pattern = Pattern.compile("\\[(\\d{2}):(\\d{2})\\.(\\d{2,3})]\\s*(.*)");
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            int minutes = Integer.parseInt(matcher.group(1));
            int seconds = Integer.parseInt(matcher.group(2));
            int fraction = Integer.parseInt(matcher.group(3));
            int startTime = (minutes * 60 * 1000) + (seconds * 1000) + (matcher.group(3).length() == 3 ? fraction : fraction * 10);
            String lyric = matcher.group(4);

            return new LyricLine(lyric, startTime, -1);
        }

        return null;
    }

    public static LyricLine parseMetadata(String line) {
        JsonObject metadata = DataUtil.gson.fromJson(line, JsonObject.class);
        int startTime = metadata.get("t").getAsInt();
        StringBuilder text = new StringBuilder();
        for (JsonElement e : metadata.get("c").getAsJsonArray()) {
            JsonObject data = e.getAsJsonObject();
            text.append(data.get("tx").getAsString());
        }
        return new LyricLine(text.toString(), startTime, -1);
    }

    public static LyricLine parseYrc(String line) {
        List<LyricChar> chars = new ArrayList<>();
        Pattern charPattern = Pattern.compile("\\((\\d+),(\\d+),\\d+\\)([^()]+)");
        Matcher charMatcher = charPattern.matcher(line);

        while (charMatcher.find()) {
            int charStartTime = Integer.parseInt(charMatcher.group(1));
            int charDuration = Integer.parseInt(charMatcher.group(2));
            String character = charMatcher.group(3);
            chars.add(new LyricChar(character, charStartTime, charDuration));
        }

        Pattern linePattern = Pattern.compile("\\[(\\d+),(\\d+)]");
        Matcher lineMatcher = linePattern.matcher(line);

        if (lineMatcher.find()) {
            int lineStartTime = Integer.parseInt(lineMatcher.group(1));
            int lineDuration = Integer.parseInt(lineMatcher.group(2));
            return new LyricLine(null, lineStartTime, lineDuration, chars);
        }

        return null;
    }

    public boolean match(int time) {
        return startTime <= time && startTime + duration >= time;
    }

    public String getStringBefore(double currentTime) {
        StringBuilder sb = new StringBuilder();
        for (LyricChar lyricChar : charList) {
            if (currentTime > lyricChar.getStartTime() + lyricChar.getDuration()) sb.append(lyricChar.getCharacter());
        }
        return sb.toString();
    }

    public void setLine(String line) {
        this.line = line;
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

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<LyricChar> getCharList() {
        return charList;
    }
}
