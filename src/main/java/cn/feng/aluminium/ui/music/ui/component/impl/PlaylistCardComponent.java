package cn.feng.aluminium.ui.music.ui.component.impl;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.font.awt.AWTFontLoader;
import cn.feng.aluminium.ui.music.api.bean.Music;
import cn.feng.aluminium.ui.music.api.bean.Playlist;
import cn.feng.aluminium.ui.music.ui.Theme;
import cn.feng.aluminium.ui.music.ui.component.AbstractComponent;
import cn.feng.aluminium.ui.music.ui.page.Pages;
import cn.feng.aluminium.ui.music.ui.page.impl.PlaylistPage;
import cn.feng.aluminium.util.animation.advanced.Animation;
import cn.feng.aluminium.util.animation.advanced.Direction;
import cn.feng.aluminium.util.animation.advanced.impl.EaseOutCubic;
import cn.feng.aluminium.util.render.ColorUtil;
import cn.feng.aluminium.util.render.RenderUtil;
import cn.feng.aluminium.util.render.shader.ShaderUtil;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/9/17
 **/
public class PlaylistCardComponent extends AbstractComponent {
    private Playlist playlist;

    private final Animation scaleAnimation = new EaseOutCubic(300, 0.04f, Direction.BACKWARDS);

    @Override
    public void render() {
        if ((hovering && !holding) && scaleAnimation.getDirection() == Direction.BACKWARDS) {
            scaleAnimation.changeDirection();
        } else if ((!hovering || holding) && scaleAnimation.getDirection() == Direction.FORWARDS)
            scaleAnimation.changeDirection();

        RenderUtil.scaleStart(renderX + width / 2f, renderY + height / 2f, 1f + scaleAnimation.getOutput().floatValue());

        ShaderUtil.drawRound(renderX, renderY, width, height, 10f, ColorUtil.applyOpacity(Color.BLACK, 0.2f));

        if (playlist != null) {
            RenderUtil.bindTexture(playlist.getCoverImage());
            ShaderUtil.drawRoundTextured(renderX + 5f, renderY + 5f, 30f, 30f, 10f, 1f);

            float textY = renderY + 50f;
            AWTFontLoader.noto(22f).bold().drawString(playlist.getTitle(), renderX + 5f, textY, Color.WHITE);
            AWTFontLoader.noto(18f).drawString(playlist.getAuthor(), renderX + 5f, textY + 20f, Theme.grey);
            AWTFontLoader.noto(18f).drawString(playlist.getDescription(), renderX + 5f, textY + 30f, Theme.grey);

            float musicY = renderY + 7f;
            for (Music music : playlist.getMusicList()) {
                int index = playlist.getMusicList().indexOf(music);
                if (index == 6) break;
                AWTFontLoader.noto(17f).drawString("#" + (index + 1) + " - " + music.getTitle(), renderX + width - 60f, musicY, ColorUtil.applyOpacity(Color.WHITE, 0.7f));
                musicY += 15f;
            }
        }
        RenderUtil.scaleEnd();
    }

    @Override
    public void mouseClicked(int mouseButton) {
        if (hovering && playlist != null && mouseButton == 0) {
            if (!Pages.playlistPageMap.containsKey(playlist.getId())) {
                Pages.playlistPageMap.put(playlist.getId(), new PlaylistPage(playlist));
            }
            Aluminium.INSTANCE.musicManager.getScreen().setCurrentPage(Pages.playlistPageMap.get(playlist.getId()));
            hovering = false;
        }
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }
}
