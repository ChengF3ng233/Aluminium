package cn.feng.aluminium.ui.music.gui.page.impl;

import cn.feng.aluminium.ui.font.FontManager;
import cn.feng.aluminium.ui.music.Theme;
import cn.feng.aluminium.ui.music.api.bean.Playlist;
import cn.feng.aluminium.ui.music.gui.component.impl.composed.PlaylistComponent;
import cn.feng.aluminium.ui.music.gui.page.Page;
import cn.feng.aluminium.util.render.ColorUtil;
import cn.feng.aluminium.util.render.RenderUtil;
import cn.feng.aluminium.util.render.shader.ShaderUtil;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/9/17
 **/
public class PlaylistPage extends Page {
    private Playlist playlist;
    private final PlaylistComponent component;

    public PlaylistPage(Playlist playlist) {
        this.playlist = playlist;
        this.component = new PlaylistComponent(playlist);
    }

    public PlaylistPage() {
        this.component = new PlaylistComponent();
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
        this.component.setPlaylist(playlist);
    }

    @Override
    public void update(float x, float y, float width, float height, int mouseX, int mouseY) {
        super.update(x, y, width, height, mouseX, mouseY);
        component.update(x, y + 120f, width, height - 125f, mouseX, mouseY);
    }

    @Override
    public void render() {
        preRender();
        RenderUtil.scissorStart(x, y, width, height);
        if (playlist != null) {
            RenderUtil.bindTexture(playlist.getCover().getCoverImage());
            ShaderUtil.drawRoundTextured(x + 5f, y + 5f, 100f, 100f, 3f, 1f);
            float titleY = FontManager.pingfangBold(45).drawTrimString(playlist.getTitle(), x + 110f, y + 7f, width - 115f, 2, 3f, Color.WHITE.darker().getRGB());
            if (playlist.getAuthor() != null) {
                FontManager.pingfangBold(16).drawString(playlist.getAuthor(), x + 110f, titleY + 10f, Color.WHITE.getRGB());
            }
            if (playlist.getDescription() != null) {
                FontManager.pingfangBold(16).drawString(playlist.getDescription(), x + 110f, titleY + 25f, Color.WHITE.darker().darker().getRGB());
            }
        } else {
            ShaderUtil.drawGradientCornerLR(x + 5f, y + 5f, 100f, 100f, 3f, ColorUtil.fade(5, 1, Theme.layerBackground, 0.7f), ColorUtil.fade(5, 3, Theme.layerBackground, 0.7f));
        }
        component.render();
        RenderUtil.scissorEnd();
        postRender();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        component.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void disableHovering() {
        super.disableHovering();
        component.disableHovering();
    }
}
