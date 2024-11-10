package cn.feng.aluminium.ui.music.gui.component.impl.slider.impl;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.music.api.player.MusicPlayer;
import cn.feng.aluminium.ui.music.gui.component.impl.slider.Slider;
import cn.feng.aluminium.util.render.shader.ShaderUtil;

import java.awt.*;

public class VolumeSlider extends Slider {
    @Override
    public void render() {
        MusicPlayer player = Aluminium.INSTANCE.musicManager.getPlayer();

        if (player.available()) {
            progressWidth = width * player.getVolume();
        }

        ShaderUtil.drawRound(x, y, width, height, 1f, new Color(200, 200, 200, 200));
        ShaderUtil.drawRound(x, y, progressWidth, height, 1f, Color.WHITE);

        if (hovering) {
            ShaderUtil.drawCircle(x + progressWidth, y + height / 2f, height, Color.WHITE);
        }

        if (dragging) {
            dragDelta = mouseX - x;
            dragDelta = Math.min(dragDelta, width);
            dragDelta = Math.max(dragDelta, 0f);
            player.setVolume(dragDelta / width);
        }

    }
}
