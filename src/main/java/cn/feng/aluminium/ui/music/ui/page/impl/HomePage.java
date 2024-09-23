package cn.feng.aluminium.ui.music.ui.page.impl;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.font.SkijaFontLoader;
import cn.feng.aluminium.ui.music.ui.component.impl.DailySongsCard;
import cn.feng.aluminium.ui.music.ui.page.AbstractPage;
import cn.feng.aluminium.ui.music.thread.FetchDailySongsThread;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class HomePage extends AbstractPage {
    private final DailySongsCard card;
    private FetchDailySongsThread thread;

    public HomePage() {
        card = new DailySongsCard();
    }

    @Override
    public void update(float renderX, float renderY, float width, float height, int mouseX, int mouseY, float centerX, float centerY, float scale) {
        super.update(renderX, renderY, width, height, mouseX, mouseY, centerX, centerY, scale);
        card.update(renderX + 20, scrolledY + 30, width - 40, 60, mouseX, mouseY);
    }

    @Override
    public void mouseReleased() {
        card.mouseReleased();
    }

    @Override
    public void render() {
        animate();
        if (card.getPlaylist() == null && thread == null) {
            thread = new FetchDailySongsThread();
            thread.start();
        }
        if (thread != null && !thread.isAlive()) {
            card.setPlaylist(thread.getResult());
            thread = null;
        }
        SkijaFontLoader.noto.bold().drawGlowString("早上好，" + Aluminium.INSTANCE.musicManager.getUser().getNickname() + "！", renderX + 20, scrolledY + 5, 25f, Color.WHITE, false);
        card.render();
        animateEnd();
    }
}
