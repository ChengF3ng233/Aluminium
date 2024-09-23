package cn.feng.aluminium.ui.music.ui.page.impl;

import cn.feng.aluminium.ui.font.SkijaFontLoader;
import cn.feng.aluminium.ui.font.awt.AWTFontLoader;
import cn.feng.aluminium.ui.font.awt.AWTFontRenderer;
import cn.feng.aluminium.ui.music.api.bean.Music;
import cn.feng.aluminium.ui.music.api.bean.Playlist;
import cn.feng.aluminium.ui.music.ui.Theme;
import cn.feng.aluminium.ui.music.ui.component.impl.MusicButtonComponent;
import cn.feng.aluminium.ui.music.ui.page.AbstractPage;
import cn.feng.aluminium.util.render.RenderUtil;
import cn.feng.aluminium.util.render.shader.ShaderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/9/17
 **/
public class PlaylistPage extends AbstractPage {
    private final Playlist playlist;
    private final List<MusicButtonComponent> musicButtonList = new ArrayList<>();

    public PlaylistPage(Playlist playlist) {
        this.playlist = playlist;
        updateButtons();
    }

    public void updateButtons() {
        musicButtonList.clear();
        for (Music music : playlist.getMusicList()) {
            musicButtonList.add(new MusicButtonComponent(music, playlist));
        }
    }

    @Override
    public void update(float renderX, float renderY, float width, float height, int mouseX, int mouseY, float centerX, float centerY, float scale) {
        super.update(renderX, renderY, width, height, mouseX, mouseY, centerX, centerY, scale);

        List<MusicButtonComponent> currentList = new ArrayList<>(musicButtonList);
        float buttonY = scrolledY + 70f;
        for (MusicButtonComponent button : currentList) {
            button.update(renderX + 5f, buttonY, width - 10f, 30f, mouseX, mouseY);
            buttonY += 35f;
        }
        pageHeight = buttonY - scrolledY;
    }

    @Override
    public void render() {
        animate();
        AWTFontRenderer noto = AWTFontLoader.noto(15f);
        RenderUtil.bindTexture(playlist.getCoverImage());
        ShaderUtil.drawRoundTextured(renderX + 5f, scrolledY + 5f, 50f, 50f, 5f, 1f);
        SkijaFontLoader.noto.bold().drawGlowString(playlist.getTitle(), renderX + 60f, scrolledY + 7f, 30f, Color.WHITE, false);
        AWTFontLoader.noto(16f).bold().drawString(playlist.getAuthor(), renderX + 60f, scrolledY + 30f, Color.WHITE);
        AWTFontLoader.noto(16f).bold().drawString(playlist.getDescription(), renderX + 60f, scrolledY + 40f, Theme.grey);
        AWTFontLoader.poppins(15f).drawString("#", renderX + 30f + 5f, scrolledY + 60f, Theme.grey);
        noto.drawString("标题", renderX + 30f + 20f + 5f, scrolledY + 60f, Theme.grey);
        noto.drawString("音乐家", renderX + 130f + 5f, scrolledY + 60f, Theme.grey);
        noto.drawString("专辑", renderX + 230f + 5f, scrolledY + 60f, Theme.grey);
        noto.drawString("时长", renderX + 350f + 5f, scrolledY + 60f, Theme.grey);
        List<MusicButtonComponent> currentList = new ArrayList<>(musicButtonList);
        currentList.forEach(MusicButtonComponent::render);
        animateEnd();
    }

    @Override
    public void mouseClicked(int mouseButton) {
        List<MusicButtonComponent> currentList = new ArrayList<>(musicButtonList);
        for (MusicButtonComponent button : currentList) {
            button.mouseClicked(mouseButton);
        }
    }
}
