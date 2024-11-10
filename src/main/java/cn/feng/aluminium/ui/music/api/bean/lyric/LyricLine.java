package cn.feng.aluminium.ui.music.api.bean.lyric;

import cn.feng.aluminium.ui.nanovg.NanoFontLoader;
import cn.feng.aluminium.ui.nanovg.NanoFontRenderer;
import cn.feng.aluminium.ui.nanovg.NanoUtil;
import cn.feng.aluminium.util.animation.advanced.Animation;
import cn.feng.aluminium.util.animation.advanced.Direction;
import cn.feng.aluminium.util.animation.advanced.composed.ColorAnimation;
import cn.feng.aluminium.util.animation.advanced.composed.CustomAnimation;
import cn.feng.aluminium.util.animation.advanced.impl.EaseBackIn;
import cn.feng.aluminium.util.animation.advanced.impl.EaseOutCubic;
import cn.feng.aluminium.util.animation.advanced.impl.SmoothStepAnimation;
import org.lwjgl.nanovg.NanoVG;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class LyricLine {
    protected final int startTime;
    private String line;
    protected int duration;
    private List<LyricChar> charList = new ArrayList<>();
    // Rendering
    protected float originX, originY, maxWidth;
    private int index;
    private boolean played;
    protected final CustomAnimation scrollAnim = new CustomAnimation(EaseOutCubic.class, 300, 0, 0);
    private final ColorAnimation colorAnim = new ColorAnimation(new Color(200, 200, 200, 200), new Color(200, 200, 200, 200), 300);

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

    public float getCurrentY() {
        return originY - scrollAnim.getOutput().floatValue();
    }

    public float renderSetup(float originX, float originY, float maxWidth, int index) {
        this.originX = originX;
        this.originY = originY;
        this.index = index;
        this.maxWidth = maxWidth;
        return NanoFontLoader.pingfang.bold().calculateChunkHeight(line, maxWidth, 3, 3f, 20f);
    }

    public boolean isPlayed() {
        return played;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }

    public void render(float time, int currentIndex) {
        NanoFontRenderer font = NanoFontLoader.pingfang.bold();
        colorAnim.change(match(time) ? Color.WHITE : new Color(200, 200, 200, Math.max(Math.min((5 - (index - currentIndex)), 4), 1) * 50));
        font.drawTrimBlurString(line, originX, originY - scrollAnim.getOutput().floatValue(), maxWidth, 3, 3f, 20f, Math.min(Math.abs(index - currentIndex) * 0.5f, 2f), NanoVG.NVG_ALIGN_LEFT | NanoVG.NVG_ALIGN_TOP, colorAnim.getOutput());
    }

    public void scrollDown(String text, int index) {
        int duration = Math.max(this.index - (index - 1), 0) * 170 + 300;
        scrollAnim.setDuration(duration);
        scrollAnim.setEndPoint(scrollAnim.getEndPoint() + NanoFontLoader.pingfang.bold().calculateChunkHeight(text, maxWidth, 3, 3f, 20f) + 5f, true);
    }

    public void reset() {
        scrollAnim.setEndPoint(0d, true);
        played = false;
    }

    public LyricChar seek(int time) {
        for (LyricChar lyricChar : charList) {
            if (lyricChar.getStartTime() <= time && lyricChar.getStartTime() + lyricChar.getDuration() >= time)
                return lyricChar;
        }
        return null;
    }

    public boolean match(float time) {
        return startTime < time && startTime + duration > time;
    }

    public boolean before(float time) {
        return startTime + duration < time;
    }

    public boolean after(float time) {
        return startTime > time;
    }

    public String getStringBefore(double currentTime) {
        StringBuilder sb = new StringBuilder();
        for (LyricChar lyricChar : charList) {
            if (currentTime > lyricChar.getStartTime() + lyricChar.getDuration()) sb.append(lyricChar.getCharacter());
        }
        return sb.toString();
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
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
