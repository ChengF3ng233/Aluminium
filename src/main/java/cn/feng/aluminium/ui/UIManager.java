package cn.feng.aluminium.ui;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.event.annotations.EventTarget;
import cn.feng.aluminium.event.events.EventChatGUI;
import cn.feng.aluminium.event.events.EventRender2D;
import cn.feng.aluminium.ui.font.FontManager;
import cn.feng.aluminium.ui.nanovg.NanoUtil;
import cn.feng.aluminium.ui.widget.Widget;
import cn.feng.aluminium.ui.widget.impl.IslandWidget;
import cn.feng.aluminium.util.Util;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class UIManager extends Util {
    private final List<Widget> widgetList = new ArrayList<>();

    public UIManager() {
        Aluminium.INSTANCE.eventManager.register(this);
    }

    public void init() {
        widgetList.add(new IslandWidget());
    }

    @EventTarget
    private void onRender2D(EventRender2D event) {
        FontManager.noto(20).drawString("Aluminium", 10, 10, Color.WHITE.getRGB());
        NanoUtil.beginFrame();
        NanoUtil.drawGradientRect(10, 10, 10, 10, Color.BLACK, Color.WHITE);
        for (Widget widget : widgetList) {
            widget.update();
            widget.render();
        }
        NanoUtil.endFrame();
    }

    @EventTarget
    private void onChatGUI(EventChatGUI e) {
        for (Widget widget : widgetList) {
            widget.onChatGUI(e.getMouseX(), e.getMouseY(), true);
        }
    }
}
