package cn.feng.aluminium.ui;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.event.annotations.EventTarget;
import cn.feng.aluminium.event.events.EventRender2D;
import cn.feng.aluminium.ui.font.NanoFontLoader;
import cn.feng.aluminium.ui.font.SkijaFontLoader;
import cn.feng.aluminium.ui.font.SkijaFontRenderer;
import cn.feng.aluminium.ui.nano.NanoUtil;
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
/*        NanoUtil.beginFrame();
        NanoFontLoader.playwrite.drawString("Aluminium", 10f, 30f, 20f, Color.WHITE);
        NanoFontLoader.script.drawString("Aluminium", 10f, 50f, 20f, Color.WHITE);
        NanoUtil.endFrame();*/
        SkijaFontLoader.playwrite.drawString("Aluminium", 10, 10, 45, Color.WHITE);
    }
}
