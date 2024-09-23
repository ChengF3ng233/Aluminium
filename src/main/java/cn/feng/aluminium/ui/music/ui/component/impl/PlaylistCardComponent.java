package cn.feng.aluminium.ui.music.ui.component.impl;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.font.awt.AWTFontLoader;
import cn.feng.aluminium.ui.font.awt.CenterType;
import cn.feng.aluminium.ui.music.api.bean.Playlist;
import cn.feng.aluminium.ui.music.ui.component.type.CardComponent;
import cn.feng.aluminium.ui.music.ui.page.Pages;
import cn.feng.aluminium.ui.music.ui.page.impl.PlaylistPage;
import cn.feng.aluminium.util.render.ColorUtil;
import cn.feng.aluminium.util.render.RenderUtil;
import cn.feng.aluminium.util.render.shader.ShaderUtil;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/9/17
 **/
public class PlaylistCardComponent extends CardComponent {
    protected Playlist playlist;

    @Override
    public void render() {
        RenderUtil.scaleStart(renderX, renderY, 1f - scaleAnimation.getOutput().floatValue());
        ShaderUtil.drawRound(renderX, renderY, width, height, 15f, ColorUtil.applyOpacity(Color.BLACK, 0.2f * (translateAnimation.getOutput().floatValue() / 3f)));
        RenderUtil.scaleEnd();
        if (playlist != null) {
            animate();
            RenderUtil.bindTexture(playlist.getCoverImage());
            ShaderUtil.drawRoundTextured(renderX, renderY, width, height, 15f, 1f);
            AWTFontLoader.noto(16f).drawCenteredString(playlist.getTitle(), renderX + width / 2f, renderY + height + 5f, Color.WHITE, CenterType.Horizontal, true);
            end();
        }
    }

    @Override
    public void mouseReleased() {
        if (hovering && playlist != null) {
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
