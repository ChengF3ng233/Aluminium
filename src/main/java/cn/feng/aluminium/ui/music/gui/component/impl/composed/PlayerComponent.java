package cn.feng.aluminium.ui.music.gui.component.impl.composed;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.font.FontManager;
import cn.feng.aluminium.ui.music.Theme;
import cn.feng.aluminium.ui.music.api.player.MusicPlayer;
import cn.feng.aluminium.ui.music.gui.component.Component;
import cn.feng.aluminium.ui.music.gui.component.impl.button.IconButton;
import cn.feng.aluminium.ui.music.gui.component.impl.button.SwitchButton;
import cn.feng.aluminium.ui.music.gui.component.impl.slider.impl.ProgressSlider;
import cn.feng.aluminium.util.data.ResourceType;
import cn.feng.aluminium.util.data.ResourceUtil;
import cn.feng.aluminium.util.render.ColorUtil;
import cn.feng.aluminium.util.render.RenderUtil;
import cn.feng.aluminium.util.render.blur.BlurUtil;
import cn.feng.aluminium.util.render.shader.ShaderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerComponent extends Component {
    private final List<IconButton> iconButtons = new ArrayList<>();
    private final ProgressSlider progressSlider;

    public PlayerComponent() {
        iconButtons.add(new IconButton(ResourceUtil.getResource("previous.png", ResourceType.ICON), () -> Aluminium.INSTANCE.musicManager.getPlayer().previous()));
        iconButtons.add(new SwitchButton(integer -> {
            if (integer == 1) {
                Aluminium.INSTANCE.musicManager.getPlayer().play();
            } else Aluminium.INSTANCE.musicManager.getPlayer().pause();
        }, ResourceUtil.getResource("pause.png", ResourceType.ICON), ResourceUtil.getResource("play.png", ResourceType.ICON), ResourceUtil.getResource("pause.png", ResourceType.ICON)));
        iconButtons.add(new IconButton(ResourceUtil.getResource("next.png", ResourceType.ICON), () -> Aluminium.INSTANCE.musicManager.getPlayer().next()));
        progressSlider = new ProgressSlider();
    }

    @Override
    public void update(float x, float y, float width, float height, int mouseX, int mouseY) {
        super.update(x, y, width, height, mouseX, mouseY);
        float buttonSize = 13f;
        float gap = 20f;
        float buttonX = x + width / 2f - buttonSize / 2f - gap - buttonSize;
        final float buttonY = y + height / 2f - buttonSize / 2f;
        for (IconButton iconButton : iconButtons) {
            iconButton.update(buttonX, buttonY, buttonSize, buttonSize, mouseX, mouseY);
            buttonX += buttonSize + gap;
        }
        progressSlider.update(x, y, width, 2f, mouseX, mouseY);
    }

    @Override
    public void render() {
        BlurUtil.processStart();
        ShaderUtil.drawRound(x, y, width, height, 3f, Color.BLACK);
        BlurUtil.blurEnd(2, 3);
        ShaderUtil.drawRound(x, y, width, height, 3f, Theme.layerBackground);
        ShaderUtil.drawVaryingRound(x, y, width, 2f, 1f, 1f, 0f, 0f, Theme.shade);
        MusicPlayer player = Aluminium.INSTANCE.musicManager.getPlayer();

        if (player.available()) {
            if (player.getMusic().getAlbum().getCover().getCoverImage() != null) {
                RenderUtil.bindTexture(player.getMusic().getAlbum().getCover().getCoverImage());
                ShaderUtil.drawRoundTextured(x + 5f, y + 5f, height - 10f, height - 10f, 3f, 1f);
            } else {
                ShaderUtil.drawGradientCornerLR(x + 5f, y + 5f, height - 10f, height - 10f, 3f, ColorUtil.fade(10, 1, new Color(40, 40, 40), 1f), ColorUtil.fade(10, 5, new Color(40, 40, 40), 1f));
            }
            FontManager.notoBold(15).drawString(player.getMusic().getTitle(), x + (height - 10f) + 8f, y + 8f, Color.WHITE.getRGB());
            FontManager.noto(15).drawString(player.getMusic().getArtist(), x + (height - 10f) + 8f, y + 17f, Color.WHITE.getRGB());
            progressSlider.render();
        }

        iconButtons.forEach(IconButton::render);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        iconButtons.forEach(it -> it.mouseClicked(mouseX, mouseY, mouseButton));
        progressSlider.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        progressSlider.mouseReleased(mouseX, mouseY, state);
    }
}
