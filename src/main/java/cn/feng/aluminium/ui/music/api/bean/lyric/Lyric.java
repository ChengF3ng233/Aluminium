package cn.feng.aluminium.ui.music.api.bean.lyric;

import cn.feng.aluminium.ui.music.api.bean.Music;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lyric {
    private final Music parent;
    private final boolean verbatim;
    private final List<LyricLine> lyricLines = new ArrayList<>();
    private final List<LyricLine> translateLines = new ArrayList<>();
    private final Map<LyricLine, LyricLine> translateMap = new HashMap<>();

    private boolean translated;

    public Lyric(Music parent, boolean verbatim) {
        this.parent = parent;
        this.verbatim = verbatim;
    }

    public LyricLine seek(float time) {
        for (LyricLine line : lyricLines) {
            if (line.getStartTime() <= time && line.getStartTime() + line.getDuration() >= time) return line;
        }
        return null;
    }

    public LyricLine seekTranslate(float time) {
        for (LyricLine line : translateLines) {
            if (line.getStartTime() <= time && line.getStartTime() + line.getDuration() >= time) return line;
        }
        return null;
    }

    public LyricLine getTranslate(LyricLine line) {
        return translateMap.getOrDefault(line, null);
    }

    public void init() {
        // 补全歌词时长
        for (int i = 0; i < lyricLines.size(); i++) {
            LyricLine line = lyricLines.get(i);
            if (line.getDuration() == -1) {
                if (i != lyricLines.size() - 1) {
                    // 以两行歌词起始时间间隔作为时长
                    line.setDuration(lyricLines.get(i + 1).getStartTime() - line.getStartTime());
                } else {
                    // 持续到歌词结尾
                    line.setDuration(parent.getDuration() - line.getStartTime());
                }
            }

            // 补全单字时长
            List<LyricChar> charList = line.getCharList();
            if (charList.isEmpty() && !line.getLine().isEmpty()) {
                charList.add(new LyricChar(line.getLine(), line.getStartTime(), line.getDuration()));
            } else if (!charList.isEmpty()) {
                for (int j = 0; j < charList.size(); j++) {
                    LyricChar lyricChar = charList.get(j);
                    if (lyricChar.getDuration() == -1) {
                        if (j == charList.size() - 1) {
                            lyricChar.setDuration(line.getDuration() - lyricChar.getStartTime());
                        } else {
                            lyricChar.setDuration(charList.get(j + 1).getStartTime() - lyricChar.getStartTime());
                        }
                    }
                }
            }

            if ((line.getLine() == null || line.getLine().isEmpty()) && !line.getCharList().isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (LyricChar lyricChar : line.getCharList()) {
                    sb.append(lyricChar.getCharacter());
                }
                line.setLine(sb.toString());
            }
        }

        // 生成翻译对照关系
        generateMap();
    }

    private void generateMap() {
        for (LyricLine lyricLine : lyricLines) {
            for (LyricLine translateLine : translateLines) {
                if (lyricLine.getStartTime() == translateLine.getStartTime()) {
                    translateLine.setDuration(lyricLine.getDuration());
                    translateMap.put(lyricLine, translateLine);
                    break;
                }
            }
        }
    }

    public boolean isVerbatim() {
        return verbatim;
    }

    public List<LyricLine> getLyricLines() {
        return lyricLines;
    }

    public List<LyricLine> getTranslateLines() {
        return translateLines;
    }

    public boolean isTranslated() {
        return translated;
    }

    public void setTranslated(boolean translated) {
        this.translated = translated;
    }
}
