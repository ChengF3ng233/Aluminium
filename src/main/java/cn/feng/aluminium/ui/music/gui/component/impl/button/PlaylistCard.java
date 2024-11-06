package cn.feng.aluminium.ui.music.gui.component.impl.button;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.font.FontManager;
import cn.feng.aluminium.ui.music.Theme;
import cn.feng.aluminium.ui.music.api.bean.Playlist;
import cn.feng.aluminium.ui.music.gui.component.Component;
import cn.feng.aluminium.ui.music.gui.page.impl.PlaylistPage;
import cn.feng.aluminium.ui.music.thread.FetchCoverThread;
import cn.feng.aluminium.util.animation.advanced.Animation;
import cn.feng.aluminium.util.animation.advanced.Direction;
import cn.feng.aluminium.util.animation.advanced.impl.DecelerateAnimation;
import cn.feng.aluminium.util.data.ResourceType;
import cn.feng.aluminium.util.data.ResourceUtil;
import cn.feng.aluminium.util.render.ColorUtil;
import cn.feng.aluminium.util.render.RenderUtil;
import cn.feng.aluminium.util.render.shader.ShaderUtil;

import java.awt.*;

public class PlaylistCard extends Component {
    private Playlist playlist;
    private final Animation iconAlpha = new DecelerateAnimation(200, 1.0, Direction.BACKWARDS);
    private Thread thread;

    public PlaylistCard(Playlist playlist) {
        this.playlist = playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    @Override
    public void render() {
        if (playlist != null) {
            if (playlist.getCover().getCoverImage() == null) {
                ShaderUtil.drawGradientCornerLR(x, y, width, height, 3f, ColorUtil.fade(10, 1, new Color(40, 40, 40), 1f), ColorUtil.fade(10, 5, new Color(40, 40, 40), 1f));
                if (thread == null) {
                    thread = new FetchCoverThread(playlist.getCover());
                    thread.start();
                } else if (!thread.isAlive()) thread = null;
            } else {
                RenderUtil.bindTexture(playlist.getCover().getCoverImage());
                ShaderUtil.drawRoundTextured(x, y, width, height, 3f, 1f);
            }
            if (hovering) {
                if (iconAlpha.getDirection().backwards()) iconAlpha.changeDirection();
            } else if (iconAlpha.getDirection().forwards()) iconAlpha.changeDirection();
            RenderUtil.drawImage(ResourceUtil.getResource("play_circle.png", ResourceType.ICON),x + width - 20f, y + height - 20f, 15f, 15f, new Color(1f, 1f, 1f, iconAlpha.getOutput().floatValue()));
            height = FontManager.pingfang(15).drawTrimString(playlist.getTitle(), x, y + height + 3f, width, 2, 1f, (hovering? Color.WHITE : Color.WHITE.darker()).getRGB()) - y;
        } else {
            ShaderUtil.drawGradientCornerLR(x, y, width, height, 3f, ColorUtil.fade(5, 1, Theme.layerBackground, 0.7f), ColorUtil.fade(5, 3, Theme.layerBackground, 0.7f));
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (hovering) {
            Aluminium.INSTANCE.musicManager.getScreen().changePage(new PlaylistPage(playlist), false);
            hovering = false;
        }
    }
}
