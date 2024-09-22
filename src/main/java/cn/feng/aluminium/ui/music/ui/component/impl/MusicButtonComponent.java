package cn.feng.aluminium.ui.music.ui.component.impl;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.font.SkijaFontLoader;
import cn.feng.aluminium.ui.font.SkijaFontRenderer;
import cn.feng.aluminium.ui.font.awt.AWTFontLoader;
import cn.feng.aluminium.ui.font.awt.AWTFontRenderer;
import cn.feng.aluminium.ui.font.awt.CenterType;
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

    private final TimerUtil timer = new TimerUtil();

    public MusicButtonComponent(Music music, Playlist parent) {
        this.music = music;
        this.parent = parent;
    }

    @Override
    public void render() {
        AWTFontRenderer poppins = AWTFontLoader.poppins(15f);
        AWTFontRenderer noto = AWTFontLoader.noto(15f);
        float textY = renderY + height / 2f;

        // Background
        ShaderUtil.drawRound(renderX, renderY, width, height, 7f, Theme.darkAlt);

        // Ripple
        StencilUtil.initStencilToWrite();
        ShaderUtil.drawRound(renderX, renderY, width, height, 7f, Color.BLACK);
        StencilUtil.readStencilBuffer(1);
        rippleList.forEach(Ripple::render);
        StencilUtil.uninitStencilBuffer();
        rippleList.removeIf(Ripple::isFinished);

        // Cover
        RenderUtil.bindTexture(music.getAlbum().getCoverImage());
        ShaderUtil.drawRoundTextured(renderX + 5f, renderY + 5f, height - 10f, height - 10f, 5f, 1f);

        // Info
        poppins.drawCenteredString((parent.getMusicList().indexOf(music) + 1) + "", renderX + height, textY, Color.WHITE, CenterType.Vertical);
        AWTFontLoader.noto(17f).drawCenteredString(music.getTitle(), renderX + height + 20f, textY, Color.WHITE, CenterType.Vertical);
        noto.drawCenteredString(music.getArtist(), renderX + 130f, textY, Color.WHITE, CenterType.Vertical);
        noto.drawCenteredString(music.getAlbum().getTitle(), renderX + 230f, textY, Color.WHITE, CenterType.Vertical);
        noto.drawCenteredString(TimeUtil.millisToMMSS(music.getDuration()), renderX + 350f, textY, Color.WHITE, CenterType.Vertical);
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
