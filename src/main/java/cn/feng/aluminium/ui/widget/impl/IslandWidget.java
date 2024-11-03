package cn.feng.aluminium.ui.widget.impl;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.music.api.bean.lyric.Lyric;
import cn.feng.aluminium.ui.music.api.bean.lyric.LyricChar;
import cn.feng.aluminium.ui.music.api.bean.lyric.LyricLine;
import cn.feng.aluminium.ui.music.api.player.MusicPlayer;
import cn.feng.aluminium.ui.nanovg.NanoFontLoader;
import cn.feng.aluminium.ui.nanovg.NanoFontRenderer;
import cn.feng.aluminium.ui.nanovg.NanoUtil;
import cn.feng.aluminium.ui.widget.Widget;
import cn.feng.aluminium.ui.widget.WidgetAlign;
import cn.feng.aluminium.util.animation.advanced.composed.CustomAnimation;
import cn.feng.aluminium.util.animation.advanced.impl.EaseBackIn;
import cn.feng.aluminium.util.misc.ChatUtil;
import cn.feng.aluminium.util.misc.TimerUtil;
import org.lwjgl.nanovg.NanoVG;

import java.awt.*;

public class IslandWidget extends Widget {
    private final CustomAnimation widthAnimation = new CustomAnimation(EaseBackIn.class, 250, 0, 0);
    private final CustomAnimation heightAnimation = new CustomAnimation(EaseBackIn.class, 250, 0, 0);
    private LyricLine lyricLine;
    private LyricChar lyricChar;
    private TimerUtil switchTimer = new TimerUtil();

    public IslandWidget() {
        super("Island", true, WidgetAlign.TOP | WidgetAlign.CENTER);
    }

    @Override
    public void render() {
        MusicPlayer player = Aluminium.INSTANCE.musicManager.getPlayer();
        if (player.available() && player.getMusic().getLyric() != null) {
            int currentTime = (int) player.getCurrentTime();
            Lyric lyric = player.getMusic().getLyric();
            LyricLine currentLine = lyric.seek(currentTime);

            if (currentLine != null && currentLine != lyricLine) {
                lyricLine = currentLine;
                switchTimer.reset();
                widthAnimation.setEndPoint(34d, true);
            }

            if (lyricLine != null) {
                LyricChar currentChar = lyricLine.seek(currentTime);

                if (currentChar != null) {
                    lyricChar = currentChar;
                }

                NanoFontRenderer font = NanoFontLoader.noto;
                float stringWidth = font.getStringWidth(lyricLine.getLine(), 15f);
                if (switchTimer.hasTimeElapsed(200)) {
                    widthAnimation.setEndPoint(stringWidth + 22f, true);
                }
                heightAnimation.setEndPoint(17f, true);
                width = widthAnimation.getOutput().floatValue();
                height = heightAnimation.getOutput().floatValue();

                float playedWidth;

                if (lyricChar == null) {
                    playedWidth = 0f;
                } else {
                    float currentPercent = Math.min((float) (currentTime - lyricChar.getStartTime()) / (float) lyricChar.getDuration(), 1f);
                    float currentWidth = font.getStringWidth(lyricChar.getCharacter(), 15f) * currentPercent;
                    float beforeWidth = font.getStringWidth(lyricLine.getStringBefore(currentTime), 15f);
                    playedWidth = currentWidth + beforeWidth;
                }

                NanoUtil.drawRoundedRect(renderX, renderY, width, height, height / 2f, new Color(20, 20, 20));
                NanoUtil.drawImageCircle(player.getMusic().getAlbum().getCover().getCoverImage(), renderX + 8.5f, renderY + 8.5f, 6f);

                NanoUtil.scaleXStart(renderX + width / 2f, renderY + height / 2f, widthAnimation.getOutput().floatValue() / (stringWidth + 22f));
                font.drawString(lyricLine.getLine(), renderX + width / 2f + 8f, renderY + height / 2f, 15f, NanoVG.NVG_ALIGN_CENTER | NanoVG.NVG_ALIGN_MIDDLE, new Color(90, 90, 90));
                NanoUtil.scissorStart(renderX + 8f + width / 2f - stringWidth / 2f, renderY, playedWidth, height, false);
                font.drawString(lyricLine.getLine(), renderX + width / 2f + 8f, renderY + height / 2f - 0.1f, 15f, NanoVG.NVG_ALIGN_CENTER | NanoVG.NVG_ALIGN_MIDDLE, Color.WHITE);
                NanoUtil.scissorEnd();
                NanoUtil.scaleEnd();
            }
        }
    }
}
