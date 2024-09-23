package cn.feng.aluminium.ui.music.ui.component.impl;

import cn.feng.aluminium.ui.font.SkijaFontLoader;
import cn.feng.aluminium.ui.music.api.bean.Music;
import cn.feng.aluminium.util.render.ColorUtil;
import cn.feng.aluminium.util.render.RenderUtil;
import cn.feng.aluminium.util.render.StencilUtil;
import cn.feng.aluminium.util.render.shader.ShaderUtil;

import java.awt.*;

public class DailySongsCard extends PlaylistCardComponent {
    @Override
    public void render() {
        RenderUtil.scaleStart(renderX, renderY, 1f - scaleAnimation.getOutput().floatValue());
        ShaderUtil.drawRound(renderX, renderY, width, height, 15f, ColorUtil.applyOpacity(Color.BLACK, 0.2f * (translateAnimation.getOutput().floatValue() / 3f)));
        RenderUtil.scaleEnd();
        if (playlist != null) {
            animate();
            ShaderUtil.drawGradientRound(renderX, renderY, width, height, 15f,
                    ColorUtil.rainbow(15, 0, 0.3f, 0.5f, 1f),
                    ColorUtil.rainbow(15, 3, 0.3f, 0.5f, 1f),
                    ColorUtil.rainbow(15, 6, 0.3f, 0.5f, 0.5f),
                    ColorUtil.rainbow(15, 9, 0.3f, 0.5f, 0.5f)
            );
            SkijaFontLoader.playwrite.drawGlowString("Daily songs", renderX + 5f, renderY + height /  2f - SkijaFontLoader.playwrite.getHeight(25f) / 4f, 25f, Color.WHITE, false);
            float coverX = renderX + 200f - 70f * (translateAnimation.getOutput().floatValue() / 3f);
            ShaderUtil.drawRound(renderX + 125f, renderY + height / 2f - 15f, width - 145f, 30f, 10f, ColorUtil.applyOpacity(Color.BLACK, 0.2f * (translateAnimation.getOutput()).floatValue() / 3f));
            StencilUtil.initStencilToWrite();
            ShaderUtil.drawRound(renderX + 130f, renderY + height / 2f - 11f, width - 155f, 22f, 8f, Color.BLACK);
            StencilUtil.readStencilBuffer(1);
            for (Music music : playlist.getMusicList()) {
                if (coverX > renderX + width) break;
                RenderUtil.bindTexture(music.getAlbum().getCoverImage());
                ShaderUtil.drawRoundTextured(coverX, renderY + height / 2f - 10f, 20f, 20f, 8f, 1f);
                coverX += 25f;
            }
            StencilUtil.uninitStencilBuffer();
            end();
        }
    }
}
