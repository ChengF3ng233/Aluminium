package cn.feng.aluminium.ui.widget.impl;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.event.annotations.EventTarget;
import cn.feng.aluminium.event.events.EventChangeMusic;
import cn.feng.aluminium.event.events.EventLyricReset;
import cn.feng.aluminium.ui.music.api.bean.lyric.LyricLeader;
import cn.feng.aluminium.ui.music.api.bean.lyric.LyricLine;
import cn.feng.aluminium.ui.music.api.player.MusicPlayer;
import cn.feng.aluminium.ui.nanovg.NanoFontLoader;
import cn.feng.aluminium.ui.nanovg.NanoUtil;
import cn.feng.aluminium.ui.nanovg.RollingText;
import cn.feng.aluminium.ui.widget.Widget;
import cn.feng.aluminium.util.data.StringUtil;
import cn.feng.aluminium.util.misc.ChatUtil;
import cn.feng.aluminium.util.render.RenderUtil;
import cn.feng.aluminium.util.render.blur.BlurUtil;
import cn.feng.aluminium.util.render.shader.ShaderUtil;

import java.awt.*;
import java.util.List;

public class MusicWidget extends Widget {
    private LyricLine lastLine;
    private RollingText title;
    private RollingText author;

    public MusicWidget() {
        super("Music", true);
    }

    @Override
    public void render2D() {
        width = 230f;
        height = 130f;
        MusicPlayer player = Aluminium.INSTANCE.musicManager.getPlayer();
        if (!player.available()) return;

        if (player.getMusic().getAlbum().getCover().getCoverImage() != null) {
            RenderUtil.bindTexture(player.getMusic().getAlbum().getCover().getCoverImage());
            ShaderUtil.drawRoundTextured(renderX + 1, renderY + 1, width - 2, height - 2, 3f, 0.7f);
        }

        ShaderUtil.drawRound(renderX + 1, renderY + 1, width - 2, height - 2, 3f, new Color(50, 50, 50, 100));
        BlurUtil.processStart();
        ShaderUtil.drawRound(renderX, renderY, width, height, 3f, Color.BLACK);
        BlurUtil.blurEnd(3, 5);

        BlurUtil.processStart();
        ShaderUtil.drawRound(renderX + 10f, renderY + 10f, 70f, 70f, 2f, Color.BLACK);
        BlurUtil.bloomEnd(2, 1);

        if (player.getMusic().getAlbum().getCover().getCoverImage() != null) {
            RenderUtil.bindTexture(player.getMusic().getAlbum().getCover().getCoverImage());
            ShaderUtil.drawRoundTextured(renderX + 10f, renderY + 10f, 70f, 70f, 2f, 1f);
        } else player.getMusic().getAlbum().getCover().load();

        NanoUtil.beginFrame();

        // Info
        title.drawString(player.getMusic().getTitle(), renderX + 10f, renderY + 85, 70f, 16f, Color.WHITE);
        author.drawString(player.getMusic().getArtist(), renderX + 10f, renderY + 95, 70f, 16f, new Color(200, 200, 200, 200));

        // Progress bar
        NanoUtil.drawRect(renderX + 10f, renderY + 110f, 70f, 3f, new Color(200, 200, 200, 100));
        NanoUtil.drawRect(renderX + 10f, renderY + 110f, 70f * player.getCurrentPercent(), 3f, Color.WHITE);
        NanoFontLoader.pingfang.drawString(StringUtil.convertMillisToMinSec((long) player.getCurrentTime()), renderX + 10f, renderY + 117f, 13f, new Color(200, 200, 200, 200));
        NanoFontLoader.pingfang.drawString(StringUtil.convertMillisToMinSec((long) (player.getMusic().getDuration() - player.getCurrentTime())), renderX + 67f, renderY + 117f, 13f, new Color(200, 200, 200, 200));

        if (player.getMusic().getLyric() == null) {
            NanoUtil.endFrame();
            return;
        }

        List<LyricLine> lyricLines = player.getMusic().getLyric().getLyricLines();

        float currentX = renderX + 85f;
        float currentY = renderY + 30f;
        for (LyricLine lyricLine : lyricLines) {
            currentY += lyricLine.renderSetup(currentX, currentY, width - 100f, lyricLines.indexOf(lyricLine)) + 5f;
        }

        float time = player.getCurrentTime();
        NanoUtil.scissorStart(renderX + 80f, renderY + 10f, width - 90f, height - 20f);

        for (LyricLine lyricLine : lyricLines) {
            if (lyricLine.match(time) && lyricLine != lastLine) {
                if (lastLine != null) {
                    scrollLyrics(lyricLines, lastLine);
                }
                lastLine = lyricLine;
            }
            if (lyricLine.getCurrentY() < renderY + height && lyricLine.getCurrentY() > renderY - 50f) {
                lyricLine.render(player.getCurrentTime(), lastLine == null ? 0 : lyricLines.indexOf(lastLine));
            }
        }


        NanoUtil.scissorEnd();
        NanoUtil.endFrame();
    }

    private void scrollLyrics(List<LyricLine> lyricLines, LyricLine lastLine) {
        if (lastLine.isPlayed()) return;
        for (LyricLine lyricLine : lyricLines) {
            lyricLine.scrollDown(lastLine.getLine(), lyricLines.indexOf(lastLine));
        }
        lastLine.setPlayed(true);
    }

    @EventTarget
    private void onChangeMusic(EventChangeMusic e) {
        title = new RollingText(NanoFontLoader.pingfang.bold());
        author = new RollingText(NanoFontLoader.pingfang);
    }

    @EventTarget
    private void onLyricReset(EventLyricReset event) {
        lastLine = null;
        List<LyricLine> lyricLines = event.getMusic().getLyric().getLyricLines();
        for (LyricLine lyricLine : lyricLines) {
            lyricLine.reset();
        }
        boolean first = true;
        for (LyricLine lyricLine : lyricLines) {
            boolean isLeader = lyricLine instanceof LyricLeader;
            if (lyricLine.before(event.getTime()) && !lyricLine.isPlayed() && (!first || isLeader)) {
                scrollLyrics(lyricLines, lyricLine);
                lastLine = lyricLine;
            }
            if (!isLeader) {
                first = false;
            }
        }
    }
}
