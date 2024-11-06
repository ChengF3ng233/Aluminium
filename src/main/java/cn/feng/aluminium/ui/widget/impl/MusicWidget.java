package cn.feng.aluminium.ui.widget.impl;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.event.annotations.EventTarget;
import cn.feng.aluminium.event.events.EventLyricReset;
import cn.feng.aluminium.ui.music.api.bean.lyric.LyricLeader;
import cn.feng.aluminium.ui.music.api.bean.lyric.LyricLine;
import cn.feng.aluminium.ui.music.api.player.MusicPlayer;
import cn.feng.aluminium.ui.nanovg.NanoFontLoader;
import cn.feng.aluminium.ui.nanovg.NanoFontRenderer;
import cn.feng.aluminium.ui.nanovg.NanoUtil;
import cn.feng.aluminium.ui.widget.Widget;
import cn.feng.aluminium.util.data.StringUtil;
import cn.feng.aluminium.util.render.ColorUtil;
import cn.feng.aluminium.util.render.RenderUtil;
import cn.feng.aluminium.util.render.blur.BlurUtil;
import cn.feng.aluminium.util.render.shader.ShaderUtil;

import java.awt.*;
import java.util.List;

public class MusicWidget extends Widget {
    private LyricLine lastLine;

    public MusicWidget() {
        super("Music", true);
    }

    @Override
    public void render2D() {
        width = 230f;
        height = 130f;
        MusicPlayer player = Aluminium.INSTANCE.musicManager.getPlayer();
        if (!player.available()) return;

        BlurUtil.processStart();
        ShaderUtil.drawRound(renderX, renderY, width, height, 3f, Color.BLACK);
        BlurUtil.blurEnd(3, 10);
        ShaderUtil.drawRound(renderX, renderY, width, height, 3f, ColorUtil.applyOpacity(RenderUtil.getMainColor(player.getMusic().getAlbum().getCover().getCoverImage()), 0.5f).darker());

        BlurUtil.processStart();
        ShaderUtil.drawRound(renderX + 10f, renderY + 10f, 70f, 70f, 2f, Color.BLACK);
        BlurUtil.bloomEnd(2, 1);

        RenderUtil.bindTexture(player.getMusic().getAlbum().getCover().getCoverImage());
        ShaderUtil.drawRoundTextured(renderX + 10f, renderY + 10f, 70f, 70f, 2f, 1f);

        NanoUtil.beginFrame();
        NanoFontRenderer font = NanoFontLoader.pingfang.bold();

        // Info
        font.drawString(player.getMusic().getTitle(), renderX + 10f, renderY + 85, 16f, Color.WHITE);
        font.drawString(player.getMusic().getArtist(), renderX + 10f, renderY + 95, 16f, new Color(200, 200, 200, 200));

        // Progress bar
        NanoUtil.drawRect(renderX + 10f, renderY + 110f, 70f, 3f, new Color(200, 200, 200, 200));
        NanoUtil.drawRect(renderX + 10f, renderY + 110f, 70f * player.getCurrentPercent(), 3f, Color.WHITE);
        font.drawString(StringUtil.convertMillisToMinSec((long) player.getCurrentTime()), renderX + 10f, renderY + 117f, 14f, Color.WHITE);
        font.drawString(StringUtil.convertMillisToMinSec((long) (player.getMusic().getDuration() - player.getCurrentTime())), renderX + 67f, renderY + 117f, 14f, Color.WHITE);

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
        NanoUtil.scissorStart(renderX + 80f, renderY + 10f, width - 95f, height - 20f);

        for (LyricLine lyricLine : lyricLines) {
            if (lyricLine.match(time) && lyricLine != lastLine) {
                if (lastLine != null) {
                    scrollAllLines(lyricLines, lastLine);
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

    private void scrollAllLines(List<LyricLine> lyricLines, LyricLine lastLine) {
        for (LyricLine lyricLine : lyricLines) {
            lyricLine.scrollDown(lastLine.getLine(), lyricLines.indexOf(lastLine));
        }
        lastLine.setPlayed(true);
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
            if (lyricLine.before(event.getTime()) && !lyricLine.isPlayed() && !first) {
                scrollAllLines(lyricLines, lyricLine);
                lastLine = lyricLine;
            }
            if (!(lyricLine instanceof LyricLeader)) {
                first = false;
            }
        }
    }
}
