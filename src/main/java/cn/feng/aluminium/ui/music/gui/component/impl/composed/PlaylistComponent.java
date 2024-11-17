package cn.feng.aluminium.ui.music.gui.component.impl.composed;

import cn.feng.aluminium.ui.font.FontManager;
import cn.feng.aluminium.ui.font.impl.UFontRenderer;
import cn.feng.aluminium.ui.music.api.bean.Music;
import cn.feng.aluminium.ui.music.api.bean.Playlist;
import cn.feng.aluminium.ui.music.gui.component.Component;
import cn.feng.aluminium.ui.music.gui.component.impl.button.MusicButton;
import cn.feng.aluminium.ui.music.thread.FetchPlaylistMusicThread;
import cn.feng.aluminium.ui.music.thread.PlaylistType;
import cn.feng.aluminium.util.render.RenderUtil;
import cn.feng.aluminium.util.render.shader.ShaderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistComponent extends Component {
    private final List<MusicButton> musicButtonList = new ArrayList<>();
    private Playlist playlist;
    private FetchPlaylistMusicThread thread;
    private PlaylistType type = PlaylistType.NORMAL;

    public PlaylistComponent(Playlist playlist) {
        this.playlist = playlist;
        init();
    }

    public PlaylistComponent() {

    }

    protected void init() {
        if (playlist.getMusicList().isEmpty()) {
            fetchMusic();
        } else updateButtons();
    }

    public PlaylistType getType() {
        return type;
    }

    public void setType(PlaylistType type) {
        this.type = type;
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
        float buttonY = y + scrollAnimation.getOutput().floatValue() + 17f;

        for (MusicButton button : currentList) {
            button.update(x + 5f, buttonY, width - 10f, 25f, mouseX, mouseY);
            buttonY += 25f;
        }

        maxScroll = Math.max(buttonY - (y + scrollAnimation.getOutput().floatValue()) + 60f - height, 0);
        handleScroll();

        if (thread != null && !thread.isAlive()) {
            thread = null;
        }
    }

    private void fetchMusic() {
        if (thread == null && !playlist.isCompletelyDownloaded()) {
            thread = new FetchPlaylistMusicThread(this, playlist, type);
            thread.start();
        }
    }

    public void reachBottom() {
        fetchMusic();
    }

    public void disableHovering() {
        musicButtonList.forEach(it -> it.setHovering(false));
    }

    @Override
    public void render() {
        UFontRenderer noto = FontManager.noto(15);
        RenderUtil.scissorStart(x, y, width, height);
        FontManager.poppins(15).drawString("#", x + 10f, y + 2.5f, new Color(150, 150,150, 200).getRGB());
        noto.drawString("标题", x + 20f + 5f, y, new Color(150, 150,150, 200).getRGB());
        noto.drawString("音乐家", x + 100f + 5f, y, new Color(150, 150,150, 200).getRGB());
        noto.drawString("专辑", x + 230f + 5f, y, new Color(150, 150,150, 200).getRGB());
        noto.drawString("时长", x + 350f + 5f, y, new Color(150, 150,150, 200).getRGB());
        if (musicButtonList.isEmpty()) {
            FontManager.notoBold(20).drawCenteredString("加载中...", x + width / 2f, y + 20f, Color.WHITE.getRGB());
        } else {
            RenderUtil.scissorStart(x, y + 15f, width, height - 15f);
            ShaderUtil.drawGradientVertical(x, y + 15f, width, 10f, 0f, new Color(0, 0, 0, 50), new Color(0, 0, 0, 0));
            List<MusicButton> currentList = new ArrayList<>(musicButtonList);
            for (MusicButton musicButton : currentList) {
                if (musicButton.getY() + musicButton.getHeight() < y + 15f) continue;
                if (musicButton.getY() > y + height) break;
                musicButton.render();
            }
            MusicButton last = currentList.get(currentList.size() - 1);
            FontManager.notoBold(15).drawCenteredString(playlist.isCompletelyDownloaded()? "已经到底了" : "正在加载更多...", x + width / 2f, last.getY() + last.getHeight() + 7f, new Color(200, 200, 200, 200).getRGB());
            RenderUtil.scissorEnd();
        }
        RenderUtil.scissorEnd();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!RenderUtil.hovering(mouseX, mouseY, x, y, width, height)) return;
        List<MusicButton> currentList = new ArrayList<>(musicButtonList);
        for (MusicButton button : currentList) {
            button.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
}
