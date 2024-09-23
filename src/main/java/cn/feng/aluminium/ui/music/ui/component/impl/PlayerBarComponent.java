package cn.feng.aluminium.ui.music.ui.component.impl;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.music.ui.component.AbstractComponent;
import cn.feng.aluminium.util.data.resource.ResourceType;
import cn.feng.aluminium.util.data.resource.ResourceUtil;
import javafx.scene.media.MediaPlayer;

/**
 * @author ChengFeng
 * @since 2024/9/17
 **/
public class PlayerBarComponent extends AbstractComponent {
    private final IconButtonComponent previous = new IconButtonComponent(ResourceUtil.getResource("previous.png", ResourceType.ICON), () -> {
        if (Aluminium.INSTANCE.musicManager.getPlayer().available()) {
            Aluminium.INSTANCE.musicManager.getPlayer().previous();
        }
    });
    private final IconButtonComponent next = new IconButtonComponent(ResourceUtil.getResource("next.png", ResourceType.ICON), () -> {
        if (Aluminium.INSTANCE.musicManager.getPlayer().available()) {
            Aluminium.INSTANCE.musicManager.getPlayer().next();
        }
    });
    private final IconButtonComponent play = new IconButtonComponent(ResourceUtil.getResource("play.png", ResourceType.ICON), () -> {
        if (Aluminium.INSTANCE.musicManager.getPlayer().available()) {
            Aluminium.INSTANCE.musicManager.getPlayer().play();
        }
    });
    private final IconButtonComponent pause = new IconButtonComponent(ResourceUtil.getResource("pause.png", ResourceType.ICON), () -> {
        if (Aluminium.INSTANCE.musicManager.getPlayer().available()) {
            Aluminium.INSTANCE.musicManager.getPlayer().pause();
        }
    });

    @Override
    public void update(float renderX, float renderY, float width, float height, int mouseX, int mouseY) {
        super.update(renderX, renderY, width, height, mouseX, mouseY);
        float gap = (width - height * 3f) / 2f;
        previous.update(renderX - gap - height, renderY, height, height, mouseX, mouseY);
        pause.update(renderX, renderY, height, height, mouseX, mouseY);
        play.update(renderX, renderY, height, height, mouseX, mouseY);
        next.update(renderX + height + gap, renderY, height, height, mouseX, mouseY);
    }

    @Override
    public void render() {
        previous.render();
        if (Aluminium.INSTANCE.musicManager.getPlayer().available() && Aluminium.INSTANCE.musicManager.getPlayer().getStatus() == MediaPlayer.Status.PLAYING) {
            pause.render();
        } else play.render();
        next.render();
    }

    @Override
    public void mouseClicked(int mouseButton) {
        previous.mouseClicked(mouseButton);
        if (Aluminium.INSTANCE.musicManager.getPlayer().available() && Aluminium.INSTANCE.musicManager.getPlayer().getStatus() == MediaPlayer.Status.PLAYING) {
            pause.mouseClicked(mouseButton);
        } else play.mouseClicked(mouseButton);
        next.mouseClicked(mouseButton);
    }
}
