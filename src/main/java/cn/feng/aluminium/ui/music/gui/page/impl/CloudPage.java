package cn.feng.aluminium.ui.music.gui.page.impl;

import cn.feng.aluminium.ui.font.FontManager;
import cn.feng.aluminium.ui.font.impl.UFontRenderer;
import cn.feng.aluminium.ui.music.api.bean.Playlist;
import cn.feng.aluminium.ui.music.gui.component.impl.composed.PlaylistComponent;
import cn.feng.aluminium.ui.music.gui.page.Page;
import cn.feng.aluminium.ui.music.thread.PlaylistType;

import java.awt.*;

public class CloudPage extends Page {
    private final PlaylistComponent component;

    public CloudPage() {
        component = new PlaylistComponent();
        component.setType(PlaylistType.CLOUD);
    }

    public void setPlaylist(Playlist playlist) {
        component.setPlaylist(playlist);
    }

    @Override
    public void update(float x, float y, float width, float height, int mouseX, int mouseY) {
        super.update(x, y, width, height, mouseX, mouseY);
        component.update(x, y + 45f, width, height - 50f, mouseX, mouseY);
    }

    @Override
    public void render() {
        FontManager.notoBold(35).drawString("我的音乐云盘", x + 5f, y + 5f, Color.WHITE.getRGB());
        component.render();
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
