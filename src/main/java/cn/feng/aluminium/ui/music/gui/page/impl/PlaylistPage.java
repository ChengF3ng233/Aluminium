package cn.feng.aluminium.ui.music.gui.page.impl;

import cn.feng.aluminium.ui.font.FontManager;
import cn.feng.aluminium.ui.font.impl.UFontRenderer;
import cn.feng.aluminium.ui.music.Theme;
import cn.feng.aluminium.ui.music.api.bean.Music;
import cn.feng.aluminium.ui.music.api.bean.Playlist;
import cn.feng.aluminium.ui.music.gui.component.impl.MusicButton;
import cn.feng.aluminium.ui.music.gui.page.Page;
import cn.feng.aluminium.ui.music.thread.FetchPlaylistMusicThread;
import cn.feng.aluminium.ui.music.thread.PlaylistType;
import cn.feng.aluminium.util.render.ColorUtil;
import cn.feng.aluminium.util.render.RenderUtil;
import cn.feng.aluminium.util.render.shader.ShaderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/9/17
 **/
public class PlaylistPage extends Page {
    private final List<MusicButton> musicButtonList = new ArrayList<>();
    protected Playlist playlist;
    protected FetchPlaylistMusicThread thread;

    public PlaylistPage(Playlist playlist) {
        this.playlist = playlist;
        init();
    }

    public PlaylistPage() {

    }

    protected void init() {
        if (playlist.getMusicList().isEmpty()) {
            fetchMusic();
        } else updateButtons();
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
        init();
    }

    public void updateButtons() {
        musicButtonList.clear();
        for (Music music : playlist.getMusicList()) {
            musicButtonList.add(new MusicButton(music, playlist));
        }
    }

    @Override
    public void update(float x, float y, float width, float height, int mouseX, int mouseY) {
        super.update(x, y, width, height, mouseX, mouseY);
        List<MusicButton> currentList = new ArrayList<>(musicButtonList);
        float buttonY = scrolledY + 125f;
        for (MusicButton button : currentList) {
            button.update(x + 5f, buttonY, width - 10f, 25f, mouseX, mouseY);
            buttonY += 25f;
        }
        pageHeight = buttonY + 5f - scrolledY + 50f;

        if (thread != null && !thread.isAlive()) {
            thread = null;
        }
    }

    protected void fetchMusic() {
        if (thread == null && !playlist.isCompletelyDownloaded()) {
            thread = new FetchPlaylistMusicThread(this, playlist, PlaylistType.NORMAL);
            thread.start();
        }
    }

    @Override
    protected void reachBottom() {
        fetchMusic();
    }

    @Override
    public void disableHovering() {
        super.disableHovering();
        musicButtonList.forEach(it -> it.setHovering(false));
    }

    @Override
    public void render() {
        UFontRenderer noto = FontManager.noto(15);
        if (playlist != null) {
            RenderUtil.bindTexture(playlist.getCoverImage());
            ShaderUtil.drawRoundTextured(x + 5f, y + 5f, 100f, 100f, 3f, 1f);
            FontManager.notoBold(50).drawString(playlist.getTitle(), x + 110f, y + 7f, Color.WHITE.darker().getRGB());
            FontManager.notoBold(16).drawString(playlist.getAuthor(), x + 110f, y + 50f, Color.WHITE.getRGB());
            FontManager.notoBold(16).drawString(playlist.getDescription(), x + 110f, y + 65f, Color.WHITE.darker().darker().getRGB());
        } else {
            ShaderUtil.drawGradientCornerLR(x + 5f, y + 5f, 100f, 100f, 3f, ColorUtil.fade(5, 1, Theme.layerBackground, 0.7f), ColorUtil.fade(5, 3, Theme.layerBackground, 0.7f));
        }
        FontManager.poppins(15).drawString("#", x + 10f, y + 110f, Color.WHITE.getRGB());
        noto.drawString("标题", x + 20f + 5f, y + 110f, Color.WHITE.getRGB());
        noto.drawString("音乐家", x + 100f + 5f, y + 110f, Color.WHITE.getRGB());
        noto.drawString("专辑", x + 230f + 5f, y + 110f, Color.WHITE.getRGB());
        noto.drawString("时长", x + 350f + 5f, y + 110f, Color.WHITE.getRGB());
        List<MusicButton> currentList = new ArrayList<>(musicButtonList);
        RenderUtil.scissorStart(x, y + 125f, width, height - 125f);
        for (MusicButton musicButton : currentList) {
            if (musicButton.getY() < y) continue;
            if (musicButton.getY() > y + height) break;
            musicButton.render();
        }
        RenderUtil.scissorEnd();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!RenderUtil.hovering(mouseX, mouseY, x, y + 125f, width, height - 125f)) return;
        List<MusicButton> currentList = new ArrayList<>(musicButtonList);
        for (MusicButton button : currentList) {
            button.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
}
