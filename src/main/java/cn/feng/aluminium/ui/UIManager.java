package cn.feng.aluminium.ui;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.event.annotations.EventTarget;
import cn.feng.aluminium.event.events.EventRender2D;
import cn.feng.aluminium.ui.font.SkijaFontLoader;
import cn.feng.aluminium.ui.music.ui.Theme;
import cn.feng.aluminium.util.Util;
import cn.feng.aluminium.util.render.shader.ShaderUtil;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class UIManager extends Util {
    public UIManager() {
        Aluminium.INSTANCE.eventManager.register(this);
    }

    @EventTarget
    private void onRender2D(EventRender2D event) {
        SkijaFontLoader.playwrite.drawGlowString("Aluminium", 10, 10, 45, Color.WHITE, false);
        ShaderUtil.drawVaryingRound(50, 50, 100, 100, 0, 0, 5, 5, Theme.light);
        ShaderUtil.drawCircle(30, 30, 10, Color.PINK);
    }
}
