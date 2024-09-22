package cn.feng.aluminium.ui.music.ui.component.impl;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.music.api.player.MusicPlayer;
import cn.feng.aluminium.ui.music.ui.Theme;
import cn.feng.aluminium.ui.music.ui.component.type.SliderComponent;
import cn.feng.aluminium.util.render.ColorUtil;
import cn.feng.aluminium.util.render.shader.ShaderUtil;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/9/17
 **/
public class VolumeSliderComponent extends SliderComponent {
    @Override
    public void render() {
        MusicPlayer player = Aluminium.INSTANCE.musicManager.getPlayer();

        if (player.available()) {
            progressWidth = (float) (width * (player.getVolume()));
        }

        ShaderUtil.drawRound(renderX, renderY, width, 2f, 0.5f, ColorUtil.applyOpacity(Color.WHITE, 0.1f));
        ShaderUtil.drawRound(renderX, renderY, dragging? dragDelta : progressWidth, 2f, 0.5f, Theme.secondary);
        ShaderUtil.drawCircle(renderX + (dragging? dragDelta : progressWidth), renderY + 1f, height / 2f, Theme.secondary);

        if (dragging) {
            dragDelta = mouseX - renderX;
            dragDelta = Math.min(dragDelta, width);
            dragDelta = Math.max(dragDelta, 0f);
        }

        if (player.available() && dragging) {
            player.setVolume((dragDelta / width));
        }
    }

    @Override
    public void mouseReleased() {
        super.mouseReleased();
    }
}
