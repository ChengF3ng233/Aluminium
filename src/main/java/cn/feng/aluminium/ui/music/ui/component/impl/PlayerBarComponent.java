package cn.feng.aluminium.ui.music.ui.component.impl;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.music.ui.component.AbstractComponent;
import cn.feng.aluminium.util.data.resource.ResourceType;
import cn.feng.aluminium.util.data.resource.ResourceUtil;

/**
 * @author ChengFeng
 * @since 2024/9/17
 **/
public class PlayerBarComponent extends AbstractComponent {
    private IconButtonComponent previous = new IconButtonComponent(ResourceUtil.getResource("previous.png", ResourceType.ICON), () -> {
        if (Aluminium.INSTANCE.musicManager.getPlayer().available()) {
            Aluminium.INSTANCE.musicManager.getPlayer().previous();
        }
    });
    private IconButtonComponent next = new IconButtonComponent(ResourceUtil.getResource("next.png", ResourceType.ICON), () -> {
        if (Aluminium.INSTANCE.musicManager.getPlayer().available()) {
            Aluminium.INSTANCE.musicManager.getPlayer().next();
        }
    });
    private IconButtonComponent play = new IconButtonComponent(ResourceUtil.getResource("play.png", ResourceType.ICON), () -> {
        if (Aluminium.INSTANCE.musicManager.getPlayer().available()) {
            Aluminium.INSTANCE.musicManager.getPlayer().play();
        }
    });
    private IconButtonComponent pause = new IconButtonComponent(ResourceUtil.getResource("pause.png", ResourceType.ICON), () -> {
        if (Aluminium.INSTANCE.musicManager.getPlayer().available()) {
            Aluminium.INSTANCE.musicManager.getPlayer().play();
        }
    });
    @Override
    public void render() {

    }
}
