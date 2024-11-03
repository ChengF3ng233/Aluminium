package cn.feng.aluminium.ui.widget.impl;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.event.annotations.EventTarget;
import cn.feng.aluminium.event.events.EventChangeMusic;
import cn.feng.aluminium.ui.music.api.bean.lyric.LyricLine;
import cn.feng.aluminium.ui.music.api.player.MusicPlayer;
import cn.feng.aluminium.ui.nanovg.NanoFontLoader;
import cn.feng.aluminium.ui.nanovg.NanoFontRenderer;
import cn.feng.aluminium.ui.nanovg.NanoUtil;
import cn.feng.aluminium.ui.widget.Widget;
import cn.feng.aluminium.util.data.StringUtil;
import cn.feng.aluminium.util.misc.ChatUtil;
import cn.feng.aluminium.util.render.ColorUtil;
import cn.feng.aluminium.util.render.RenderUtil;
import cn.feng.aluminium.util.render.blur.BlurUtil;
import cn.feng.aluminium.util.render.shader.ShaderUtil;

import java.awt.*;
import java.util.List;

public class MusicWidget extends Widget {
    public MusicWidget() {
        super("Music", true);
    }

    private LyricLine lastLine;

    @Override
    public void render2D() {
        width = 230f;
        height = 130f;
        MusicPlayer player = Aluminium.INSTANCE.musicManager.getPlayer();
        if (!player.available()) return;

        BlurUtil.processStart();
        ShaderUtil.drawRound(renderX, renderY, width, height, 3f, Color.BLACK);
        BlurUtil.blurEnd(2, 2);
        ShaderUtil.drawRound(renderX, renderY, width, height, 3f, ColorUtil.applyOpacity(RenderUtil.getMainColor(player.getMusic().getAlbum().getCover().getCoverImage()), 0.5f).darker());

        BlurUtil.processStart();
        ShaderUtil.drawRound(renderX + 10f, renderY + 10f, 70f, 70f, 2f, Color.BLACK);
        BlurUtil.bloomEnd(2, 1);

        RenderUtil.bindTexture(player.getMusic().getAlbum().getCover().getCoverImage());
        ShaderUtil.drawRoundTextured(renderX + 10f, renderY + 10f, 70f, 70f, 2f, 1f);

        NanoUtil.beginFrame();
        NanoFontRenderer font = NanoFontLoader.pingfang.bold();
        font.drawString(player.getMusic().getTitle(), renderX + 10f, renderY + 85, 16f, Color.WHITE);
        font.drawString(player.getMusic().getArtist(), renderX + 10f, renderY + 95, 16f, new Color(200, 200, 200, 200));

        if (player.getMusic().getLyric() == null) return;
        List<LyricLine> lyricLines = player.getMusic().getLyric().getLyricLines();

        float currentX = renderX + 85f;
        float currentY = renderY + 30f;
        for (LyricLine lyricLine : lyricLines) {
            lyricLine.renderSetup(currentX, currentY, lyricLines.indexOf(lyricLine));
            currentY += 20f;
        }

        NanoUtil.scissorStart(renderX + 85f, renderY + 10f, width - 95f, height - 20f);
        for (LyricLine lyricLine : lyricLines) {
            if (lyricLine.match((float) player.getCurrentTime()) && lyricLine != lastLine) {
                if (lastLine != null && !lyricLine.isPlayed()) {
                    for (LyricLine line : lyricLines) {
                        line.scrollDown(lastLine == null? 0 : lyricLines.indexOf(lastLine));
                    }
                    lyricLine.setPlayed(true);
                }
                lastLine = lyricLine;
            }
            lyricLine.render((float) player.getCurrentTime(), lastLine == null? 0 : lyricLines.indexOf(lastLine));
        }
        NanoUtil.scissorEnd();
        NanoUtil.drawRect(renderX + 10f, renderY + 110f, 70f, 3f, new Color(200, 200, 200, 200));
        NanoUtil.drawRect(renderX + 10f, renderY + 110f, 70f * (float) (player.getCurrentPercent()), 3f, Color.WHITE);
        font.drawString(StringUtil.convertMillisToMinSec((long) player.getCurrentTime()), renderX + 10f, renderY + 117f, 14f, Color.WHITE);
        font.drawString(StringUtil.convertMillisToMinSec((long) (player.getMusic().getDuration() - player.getCurrentTime())), renderX + 67f, renderY + 117f, 14f, Color.WHITE);
        NanoUtil.endFrame();
    }

    @EventTarget
    private void onChangeMusic(EventChangeMusic event) {
        lastLine = null;
        for (LyricLine lyricLine : event.getMusic().getLyric().getLyricLines()) {
            lyricLine.reset();
        }
    }
}
