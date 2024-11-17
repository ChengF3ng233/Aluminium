package cn.feng.aluminium.ui.widget.impl.island;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.event.annotations.EventTarget;
import cn.feng.aluminium.event.events.EventChangeMusic;
import cn.feng.aluminium.event.events.EventPlayerStatus;
import cn.feng.aluminium.module.modules.visual.hud.HUD;
import cn.feng.aluminium.ui.music.api.bean.lyric.Lyric;
import cn.feng.aluminium.ui.music.api.bean.lyric.LyricChar;
import cn.feng.aluminium.ui.music.api.bean.lyric.LyricLine;
import cn.feng.aluminium.ui.music.api.player.MusicPlayer;
import cn.feng.aluminium.ui.nanovg.NanoFontLoader;
import cn.feng.aluminium.ui.nanovg.NanoFontRenderer;
import cn.feng.aluminium.ui.nanovg.NanoUtil;
import cn.feng.aluminium.ui.widget.Widget;
import cn.feng.aluminium.ui.widget.WidgetAlign;
import cn.feng.aluminium.util.animation.advanced.composed.CustomAnimation;
import cn.feng.aluminium.util.animation.advanced.impl.EaseOutCubic;
import cn.feng.aluminium.util.misc.TimerUtil;
import net.minecraft.client.Minecraft;
import org.lwjgl.nanovg.NanoVG;

import java.awt.*;

public class IslandWidget extends Widget {
    private final CustomAnimation widthAnimation = new CustomAnimation(EaseOutCubic.class, 300, 0, 0);
    private final CustomAnimation heightAnimation = new CustomAnimation(EaseOutCubic.class, 300, 0, 0);
    private IslandState state = IslandState.NORMAL;

    // Music
    private final TimerUtil changeMusicTimer = new TimerUtil();
    private LyricLine lyricLine;
    private LyricChar lyricChar;

    public IslandWidget() {
        super("Island", true, WidgetAlign.TOP | WidgetAlign.CENTER);
        x = 0.5f;
        y = 0.01f;
    }

    @Override
    public void renderNanoVG() {
        MusicPlayer player = Aluminium.INSTANCE.musicManager.getPlayer();
        NanoFontRenderer font = NanoFontLoader.pingfang;

        // 背景
        NanoUtil.drawRoundedRect(renderX, renderY, width, height, height / 2f, new Color(20, 20, 20, 200));
        width = widthAnimation.getOutput().floatValue();
        height = heightAnimation.getOutput().floatValue();

        switch (state) {
            case LYRIC: {
                // 歌词播放
                if (!player.available()) return;
                if (player.getMusic().getLyric() == null) {
                    changeState(IslandState.NORMAL);
                }


                if (player.getMusic().getAlbum().getCover().getCoverImage() != null) {
                    NanoUtil.drawImageCircle(player.getMusic().getAlbum().getCover().getCoverImage(), renderX + height / 2f, renderY + height / 2f, height / 2f - 1f);
                }

                int currentTime = (int) player.getCurrentTime();
                Lyric lyric = player.getMusic().getLyric();
                LyricLine currentLine = lyric.seek(currentTime);

                if (currentLine != null && currentLine != lyricLine && !currentLine.getLine().isEmpty()) {
                    lyricLine = currentLine;
                }

                if (lyricLine != null) {
                    LyricChar currentChar = lyricLine.seek(currentTime);

                    if (currentChar != null) {
                        lyricChar = currentChar;
                    }

                    float stringWidth = font.getStringWidth(lyricLine.getLine(), 15f);
                    widthAnimation.setEndPoint(stringWidth + 22f, true);
                    heightAnimation.setEndPoint(17f, true);

                    float playedWidth;

                    if (lyricChar == null) {
                        playedWidth = 0f;
                    } else {
                        float currentPercent = Math.min((float) (currentTime - lyricChar.getStartTime()) / (float) lyricChar.getDuration(), 1f);
                        float currentWidth = font.getStringWidth(lyricChar.getCharacter(), 15f) * currentPercent;
                        float beforeWidth = font.getStringWidth(lyricLine.getStringBefore(currentTime), 15f);
                        playedWidth = currentWidth + beforeWidth;
                    }

                    NanoUtil.scissorStart(renderX, renderY, width, height);
                    font.drawString(lyricLine.getLine(), renderX + width / 2f + 8f, renderY + height / 2f, 15f, NanoVG.NVG_ALIGN_CENTER | NanoVG.NVG_ALIGN_MIDDLE, new Color(200, 200, 200, 200));
                    NanoUtil.scissorStart(renderX + 8f + width / 2f - stringWidth / 2f - 1f, renderY, playedWidth + 1f, height, false);
                    font.drawString(lyricLine.getLine(), renderX + width / 2f + 8f, renderY + height / 2f - 0.1f, 15f, NanoVG.NVG_ALIGN_CENTER | NanoVG.NVG_ALIGN_MIDDLE, Color.WHITE);
                    NanoUtil.scissorEnd();
                    NanoUtil.scissorEnd();
                }
                break;
            }

            case CHANGE_MUSIC: {
                // 歌曲切换
                if (!player.available()) return;

                if (player.getMusic().getAlbum().getCover().getCoverImage() != null) {
                    NanoUtil.drawImageCircle(player.getMusic().getAlbum().getCover().getCoverImage(), renderX + height / 2f, renderY + height / 2f, height / 2f - 1f);
                }

                if (!changeMusicTimer.hasTimeElapsed(3000)) {
                    String text = "下一首：" + player.getMusic().getTitle();
                    heightAnimation.setEndPoint(25, true);
                    widthAnimation.setEndPoint(30 + font.getStringWidth(text) + 5f, true);
                    font.drawString(text, renderX + 30f, renderY + height / 2f, 15f, NanoVG.NVG_ALIGN_LEFT | NanoVG.NVG_ALIGN_MIDDLE, Color.WHITE);
                } else {
                    heightAnimation.setEndPoint(17, true);
                    changeState(IslandState.LYRIC);
                }
                break;
            }

            case NORMAL: {
                float width = font.bold().getStringWidth("Aluminium", 17f) + font.bold().getStringWidth(" | " + mc.thePlayer.getName() + " | " + Minecraft.getDebugFPS() + "fps", 17f) + 10f;
                font.bold().drawGlowString("Aluminium", renderX + 5f, renderY + height / 2f, 17f, NanoVG.NVG_ALIGN_LEFT | NanoVG.NVG_ALIGN_MIDDLE, HUD.getAccentColor());
                font.bold().drawString( " | " + mc.thePlayer.getName() + " | " + Minecraft.getDebugFPS() + "fps", renderX + width - 5f, renderY + height / 2f, 17f, NanoVG.NVG_ALIGN_RIGHT | NanoVG.NVG_ALIGN_MIDDLE, Color.WHITE);
                widthAnimation.setEndPoint(width, true);
                heightAnimation.setEndPoint(20f, true);
                break;
            }
        }
    }

    public void changeState(IslandState state) {
        this.state = state;
    }

    @EventTarget
    private void onChangeMusic(EventChangeMusic event) {
        changeMusicTimer.reset();
        changeState(IslandState.CHANGE_MUSIC);
    }

    @EventTarget
    private void onPlayerStatus(EventPlayerStatus e) {
        switch (e.getStatus()) {
            case PLAYING: {
                changeState(IslandState.LYRIC);
                break;
            }

            case PAUSED: {
                changeState(IslandState.NORMAL);
                break;
            }
        }
    }
}
