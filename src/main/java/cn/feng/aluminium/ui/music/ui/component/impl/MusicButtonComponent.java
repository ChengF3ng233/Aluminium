package cn.feng.aluminium.ui.music.ui.component.impl;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.font.SkijaFontLoader;
import cn.feng.aluminium.ui.music.api.bean.Music;
import cn.feng.aluminium.ui.music.api.bean.Playlist;
import cn.feng.aluminium.ui.music.ui.Theme;
import cn.feng.aluminium.ui.music.ui.component.AbstractComponent;
import cn.feng.aluminium.util.misc.TimeUtil;
import cn.feng.aluminium.util.misc.TimerUtil;
import cn.feng.aluminium.util.render.RenderUtil;
import cn.feng.aluminium.util.render.StencilUtil;
import cn.feng.aluminium.util.render.shader.ShaderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/9/17
 **/
public class MusicButtonComponent extends AbstractComponent {
    private final Music music;
    private final Playlist parent;
    private final List<Ripple> rippleList = new ArrayList<>();

    private TimerUtil timer = new TimerUtil();

    public MusicButtonComponent(Music music, Playlist parent) {
        this.music = music;
        this.parent = parent;
    }

    @Override
    public void render() {
        ShaderUtil.drawRound(renderX, renderY, width, height, 7f, Theme.darkAlt);
        float fontHeight = SkijaFontLoader.noto.getHeight(17f);
        float textY = renderY + height / 2f + 2f - fontHeight / 4f;
        StencilUtil.initStencilToWrite();
        ShaderUtil.drawRound(renderX, renderY, width, height, 7f, Color.BLACK);
        StencilUtil.readStencilBuffer(1);
        rippleList.forEach(Ripple::render);
        StencilUtil.uninitStencilBuffer();
        RenderUtil.bindTexture(music.getAlbum().getCoverImage());
        ShaderUtil.drawRoundTextured(renderX + 5f, renderY + 5f, height - 10f, height - 10f, 5f, 1f);
        SkijaFontLoader.poppins.drawString((parent.getMusicList().indexOf(music) + 1) + "", renderX + height, renderY + height / 2f + 2f - SkijaFontLoader.poppins.getHeight(15f) / 4f, 15f, Color.WHITE, true);
        SkijaFontLoader.noto.drawString(music.getTitle(), renderX + height + 20f, textY, 17f, Color.WHITE, true);
        SkijaFontLoader.noto.drawString(music.getArtist(), renderX + 130f, textY, 15f, Color.WHITE, false);
        SkijaFontLoader.noto.drawString(music.getAlbum().getTitle(), renderX + 230f, textY, 15f, Color.WHITE, false);
        SkijaFontLoader.noto.drawString(TimeUtil.millisToMMSS(music.getDuration()), renderX + 350f, textY, 15f, Color.WHITE, false);

        rippleList.removeIf(Ripple::isFinished);
    }

    @Override
    public void mouseClicked(int mouseButton) {
        if (hovering && mouseButton == 0) {
            if (!timer.hasTimeElapsed(1000)) {
                Aluminium.INSTANCE.musicManager.getPlayer().setCurrentMusic(music);
                Aluminium.INSTANCE.musicManager.getPlayer().setCurrentMusicList(parent.getMusicList());
            }
            timer.reset();
            rippleList.add(new Ripple(System.currentTimeMillis(), mouseX, mouseY, width));
        }
    }
}
