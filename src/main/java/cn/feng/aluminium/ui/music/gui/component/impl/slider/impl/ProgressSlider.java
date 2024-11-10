package cn.feng.aluminium.ui.music.gui.component.impl.slider.impl;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.music.api.player.MusicPlayer;
import cn.feng.aluminium.ui.music.gui.component.impl.slider.Slider;
import cn.feng.aluminium.util.render.shader.ShaderUtil;

import java.awt.*;

public class ProgressSlider extends Slider {
    @Override
    public void render() {
        MusicPlayer player = Aluminium.INSTANCE.musicManager.getPlayer();

        if (player.available()) {
            progressWidth = width * player.getCurrentPercent();
        }

        ShaderUtil.drawVaryingRound(x, y, width, height, 1f, 1f, 0f, 0f, new Color(50, 50, 50, 100));
        ShaderUtil.drawVaryingRound(x, y, dragging ? dragDelta : progressWidth, height, 1f, 1f, 0f, 0f, Color.WHITE);

        if (dragging) {
            dragDelta = mouseX - x;
            dragDelta = Math.min(dragDelta, width);
            dragDelta = Math.max(dragDelta, 0f);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        MusicPlayer player = Aluminium.INSTANCE.musicManager.getPlayer();

        if (player.available() && dragging) {
            player.seek(((dragDelta / width) * player.getMusic().getDuration()));
        }
        super.mouseReleased(mouseX, mouseY, state);
    }
}
