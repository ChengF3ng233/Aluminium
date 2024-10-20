package cn.feng.aluminium.ui;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.event.annotations.EventTarget;
import cn.feng.aluminium.event.events.EventRender2D;
import cn.feng.aluminium.ui.font.FontManager;
import cn.feng.aluminium.util.Util;

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
        FontManager.noto(20).drawString("Aluminium", 10, 10, Color.WHITE.getRGB());
    }
}
