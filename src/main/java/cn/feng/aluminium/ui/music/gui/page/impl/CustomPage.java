package cn.feng.aluminium.ui.music.gui.page.impl;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.font.FontManager;
import cn.feng.aluminium.ui.music.api.bean.Playlist;
import cn.feng.aluminium.ui.music.gui.component.impl.button.PlaylistCard;
import cn.feng.aluminium.ui.music.gui.page.Page;
import cn.feng.aluminium.util.render.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CustomPage extends Page {
    private final List<PlaylistCard> likes = new ArrayList<>();
    private final List<PlaylistCard> recommends = new ArrayList<>();
    private final List<PlaylistCard> radars = new ArrayList<>();

    public void addLike(Playlist playlist) {
        likes.add(new PlaylistCard(playlist));
    }

    public void addRecommend(Playlist playlist) {
        recommends.add(new PlaylistCard(playlist));
    }

    public void addRadar(Playlist playlist) {
        radars.add(new PlaylistCard(playlist));
    }

    @Override
    public void update(float x, float y, float width, float height, int mouseX, int mouseY) {
        super.update(x, y, width, height, mouseX, mouseY);

        int likeSize = likes.size();
        int radarSize = radars.size();
        int recommendSize = recommends.size();


        final float cardSize = 100f;
        final float gap = 8f;
        float cardY = scrolledY + 25f;

        if (likeSize > 0) {
            for (int i = 0; i < likeSize; i++) {
                float cardX = i % 4 == 0 ? x : i % 4 == 1 ? x + cardSize + gap : i % 4 == 2 ? x + cardSize * 2 + gap * 2 : x + cardSize * 3 + gap * 3;
                if (i % 4 == 0 && i != 0) {
                    cardY += likes.get(i).getHeight() + gap;
                }
                likes.get(i).update(cardX, cardY, cardSize, cardSize, mouseX, mouseY);
            }

            cardY += cardSize + gap + 35f;
        }

        if (radarSize > 0) {
            for (int i = 0; i < radarSize; i++) {
                float cardX = i % 4 == 0 ? x : i % 4 == 1 ? x + cardSize + gap : i % 4 == 2 ? x + cardSize * 2 + gap * 2 : x + cardSize * 3 + gap * 3;
                if (i % 4 == 0 && i != 0) {
                    cardY += radars.get(i).getHeight() + gap;
                }
                radars.get(i).update(cardX, cardY, cardSize, cardSize, mouseX, mouseY);
            }

            cardY += cardSize + gap + 35f;
        }

        if (recommendSize > 0) {
            for (int i = 0; i < recommendSize; i++) {
                float cardX = i % 4 == 0 ? x : i % 4 == 1 ? x + cardSize + gap : i % 4 == 2 ? x + cardSize * 2 + gap * 2 : x + cardSize * 3 + gap * 3;
                if (i % 4 == 0 && i != 0) {
                    cardY += recommends.get(i).getHeight() + gap;
                }
                recommends.get(i).update(cardX, cardY, cardSize, cardSize, mouseX, mouseY);
            }

            maxScroll = Math.max(cardY + recommends.get(recommendSize - 1).getHeight() - scrolledY - height, 0) + 70f;
        }

    }

    @Override
    public void render() {
        preRender();
        RenderUtil.scissorStart(x, y, width, height);
        if (!likes.isEmpty()) {
            FontManager.notoBold(25).drawString("我的歌单", x + 3f, scrolledY + 3f, Color.WHITE.getRGB());
            List<PlaylistCard> likeList = new ArrayList<>(likes);
            likeList.forEach(PlaylistCard::render);
        }
        if (!radars.isEmpty()) {
            FontManager.notoBold(25).drawString(Aluminium.INSTANCE.musicManager.getUser().getNickname() + "的雷达歌单", x + 3f, radars.get(0).getY() - 20f, Color.WHITE.getRGB());
            List<PlaylistCard> radarList = new ArrayList<>(radars);
            radarList.forEach(PlaylistCard::render);
        }
        if (!recommends.isEmpty()) {
            FontManager.notoBold(25).drawString("【私人定制】推荐歌单", x + 3f, recommends.get(0).getY() - 20f, Color.WHITE.getRGB());
            List<PlaylistCard> recommendList = new ArrayList<>(recommends);
            recommendList.forEach(PlaylistCard::render);
        }
        RenderUtil.scissorEnd();
        postRender();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!recommends.isEmpty()) {
            for (PlaylistCard card : recommends) {
                card.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
        if (!radars.isEmpty()) {
            for (PlaylistCard card : radars) {
                card.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
        if (!likes.isEmpty()) {
            for (PlaylistCard like : likes) {
                like.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void disableHovering() {
        super.disableHovering();
        if (!recommends.isEmpty())
            recommends.forEach(it -> it.setHovering(false));
        if (!radars.isEmpty())
            radars.forEach(it -> it.setHovering(false));
        if (!likes.isEmpty())
            likes.forEach(it -> it.setHovering(false));
    }
}
