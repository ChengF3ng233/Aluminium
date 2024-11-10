package cn.feng.aluminium.ui.music.gui.page.impl;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.font.FontManager;
import cn.feng.aluminium.ui.music.api.bean.Playlist;
import cn.feng.aluminium.ui.music.gui.component.impl.button.PlaylistCard;
import cn.feng.aluminium.ui.music.gui.page.Page;
import cn.feng.aluminium.util.data.StringUtil;
import cn.feng.aluminium.util.render.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RecommendPage extends Page {
    private final List<PlaylistCard> cards = new ArrayList<>();

    public void addPlaylist(Playlist playlist) {
        cards.add(new PlaylistCard(playlist));
    }

    @Override
    public void update(float x, float y, float width, float height, int mouseX, int mouseY) {
        super.update(x, y, width, height, mouseX, mouseY);

        int size = cards.size();
        final float cardSize = 100f;
        final float gap = 8f;
        float cardY = scrolledY + 25f;
        for (int i = 0; i < size; i++) {
            float cardX = i % 4 == 0 ? x : i % 4 == 1 ? x + cardSize + gap : i % 4 == 2 ? x + cardSize * 2 + gap * 2 : x + cardSize * 3 + gap * 3;
            if (i % 4 == 0 && i != 0) {
                cardY += cards.get(i).getHeight() + gap;
            }
            cards.get(i).update(cardX, cardY, cardSize, cardSize, mouseX, mouseY);
        }

        maxScroll = Math.max(cardY + cards.get(cards.size() - 1).getHeight() - scrolledY - height, 0) + 70f;
    }

    @Override
    public void render() {
        preRender();
        RenderUtil.scissorStart(x, y, width, height);
        FontManager.notoBold(20).drawString(StringUtil.getGreeting() + "，" + Aluminium.INSTANCE.musicManager.getUser().getNickname(), x + 3f, scrolledY + 3f, Color.WHITE.getRGB());
        List<PlaylistCard> cardList = new ArrayList<>(cards);
        for (PlaylistCard playlistCard : cardList) {
            playlistCard.render();
        }
        RenderUtil.scissorEnd();
        postRender();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (PlaylistCard card : cards) {
            card.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void disableHovering() {
        super.disableHovering();
        cards.forEach(it -> it.setHovering(false));
    }
}
