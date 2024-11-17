package cn.feng.aluminium.ui;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.event.annotations.EventTarget;
import cn.feng.aluminium.event.events.EventChatGUI;
import cn.feng.aluminium.event.events.EventRender2D;
import cn.feng.aluminium.ui.clickgui.ClickGui;
import cn.feng.aluminium.ui.font.FontManager;
import cn.feng.aluminium.ui.nanovg.NanoUtil;
import cn.feng.aluminium.ui.widget.Widget;
import cn.feng.aluminium.ui.widget.impl.island.IslandWidget;
import cn.feng.aluminium.ui.widget.impl.MusicWidget;
import cn.feng.aluminium.util.Util;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class UIManager extends Util {
    private final List<Widget> widgetList = new ArrayList<>();
    private GuiScreen clickGui;

    public UIManager() {
        Aluminium.INSTANCE.eventManager.register(this);
    }

    public void init() {
        register(new IslandWidget());
        register(new MusicWidget());
        clickGui = new ClickGui();
    }

    private void register(Widget widget) {
        Aluminium.INSTANCE.moduleManager.register(widget);
        widgetList.add(widget);
        Aluminium.INSTANCE.eventManager.register(widget);
    }

    @EventTarget
    private void onRender2D(EventRender2D event) {
        FontManager.noto(20).drawString("Aluminium", 10, 10, Color.WHITE.getRGB());
        for (Widget widget : widgetList) {
            widget.update();
        }

        List<Widget> widgets = widgetList.stream().filter(it -> Aluminium.INSTANCE.moduleManager.getModule(it).isEnabled()).collect(Collectors.toList());
        NanoUtil.beginFrame();
        widgets.forEach(Widget::renderNanoVG);
        NanoUtil.endFrame();
        widgets.forEach(Widget::render2D);
    }

    @EventTarget
    private void onChatGUI(EventChatGUI event) {
        Widget draggingWidget = null;
        List<Widget> widgets = widgetList.stream().filter(it -> Aluminium.INSTANCE.moduleManager.getModule(it).isEnabled()).collect(Collectors.toList());
        for (Widget widget : widgetList) {
            if (widgets.contains(widget) && widget.dragging) {
                draggingWidget = widget;
                break;
            }
        }

        for (Widget widget : widgetList) {
            if (widgets.contains(widget)) {
                widget.onChatGUI(event.getMouseX(), event.getMouseY(), (draggingWidget == null || draggingWidget == widget));
                if (widget.dragging) draggingWidget = widget;
            }
        }
    }

    public GuiScreen getClickGui() {
        return clickGui;
    }
}
