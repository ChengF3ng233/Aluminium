package cn.feng.aluminium.ui.music.gui.component.impl.button;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.font.FontManager;
import cn.feng.aluminium.ui.font.impl.UFontRenderer;
import cn.feng.aluminium.ui.music.api.bean.Music;
import cn.feng.aluminium.ui.music.api.bean.Playlist;
import cn.feng.aluminium.ui.music.gui.component.Component;
import cn.feng.aluminium.ui.music.gui.component.impl.Ripple;
import cn.feng.aluminium.util.data.ResourceType;
import cn.feng.aluminium.util.data.ResourceUtil;
import cn.feng.aluminium.util.misc.TimeUtil;
import cn.feng.aluminium.util.misc.TimerUtil;
import cn.feng.aluminium.util.render.RenderUtil;
import cn.feng.aluminium.util.render.StencilUtil;
import cn.feng.aluminium.util.render.shader.ShaderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MusicButton extends Component {
    private final Music music;
    private final Playlist parent;
    private final List<Ripple> rippleList = new ArrayList<>();

    private final TimerUtil timer = new TimerUtil();

    public MusicButton(Music music, Playlist parent) {
        this.music = music;
        this.parent = parent;
    }

    @Override
    public void render() {
        UFontRenderer poppins = FontManager.poppinsBold(14);
        UFontRenderer notoBold = FontManager.notoBold(14);
        UFontRenderer noto = FontManager.noto(14);
        float textY = y + height / 2f;

        // Background
        if (hovering || Aluminium.INSTANCE.musicManager.getPlayer().getMusic() == music) {
            ShaderUtil.drawRound(x, y, width, height, 3f, new Color(200, 200, 200, 20));
        }

        // Ripple
        StencilUtil.initStencilToWrite();
        ShaderUtil.drawRound(x, y, width, height, 3f, Color.BLACK);
        StencilUtil.readStencilBuffer(1);
        rippleList.forEach(Ripple::render);
        StencilUtil.uninitStencilBuffer();
        rippleList.removeIf(Ripple::isFinished);

        // Info
        if (Aluminium.INSTANCE.musicManager.getPlayer().getMusic() == music) {
            RenderUtil.drawImage(ResourceUtil.getResource("equalizer.png", ResourceType.ICON), x + 5f, textY - 4f, 8f, 8f);
        } else {
            poppins.drawCenteredStringV((parent.getMusicList().indexOf(music) + 1) + "", x + 5f, textY + 1f, new Color(170, 170, 170, 170).getRGB());
        }
        notoBold.drawCenteredStringV(notoBold.trimStringToWidth(music.getTitle(), 75f), x + 20f, textY, Color.WHITE.getRGB());
        noto.drawCenteredStringV(noto.trimStringToWidth(music.getArtist(), 125f), x + 100f, textY, new Color(200, 200, 200, 200).getRGB());
        noto.drawCenteredStringV(noto.trimStringToWidth(music.getAlbum().getTitle(), 115f), x + 230f, textY, new Color(200, 200, 200, 200).getRGB());
        poppins.drawCenteredStringV(TimeUtil.millisToMMSS(music.getDuration()), x + 350f, textY, new Color(200, 200, 200, 100).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (hovering && mouseButton == 0) {
            if (!timer.hasTimeElapsed(1000)) {
                Aluminium.INSTANCE.musicManager.getPlayer().setMusic(music);
                Aluminium.INSTANCE.musicManager.getPlayer().setMusicList(parent.getMusicList());
            }
            timer.reset();
            rippleList.add(new Ripple(System.currentTimeMillis(), mouseX, mouseY, width));
        }
    }
}
