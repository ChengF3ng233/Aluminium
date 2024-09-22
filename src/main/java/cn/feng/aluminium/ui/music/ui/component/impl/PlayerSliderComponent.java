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
public class PlayerSliderComponent extends SliderComponent {
    @Override
    public void render() {
        MusicPlayer player = Aluminium.INSTANCE.musicManager.getPlayer();

        if (player.available()) {
            progressWidth = width * (player.getCurrentTime() / (float) player.getCurrentMusic().getDuration());
        }

        ShaderUtil.drawRound(renderX, renderY, width, 2f, 1, ColorUtil.applyOpacity(Color.WHITE, 0.1f));
        ShaderUtil.drawRound(renderX, renderY, dragging? dragDelta : progressWidth, 2f, 1f, Theme.secondary);
        ShaderUtil.drawCircle(renderX + (dragging? dragDelta : progressWidth), renderY + 1f, height / 2f + 0.5f, Color.WHITE);
        ShaderUtil.drawCircle(renderX + (dragging? dragDelta : progressWidth), renderY + 1f, height / 2f, Theme.secondary);

        if (dragging) {
            dragDelta = mouseX - renderX;
            dragDelta = Math.min(dragDelta, width);
            dragDelta = Math.max(dragDelta, 0f);
        }
    }

    @Override
    public void mouseReleased() {
        MusicPlayer player = Aluminium.INSTANCE.musicManager.getPlayer();

        if (player.available() && dragging) {
            player.seek(((dragDelta / width) * player.getCurrentMusic().getDuration()));
        }

        super.mouseReleased();
    }
}
