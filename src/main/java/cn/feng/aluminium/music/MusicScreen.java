package cn.feng.aluminium.music;

import cn.feng.aluminium.ui.font.SkijaFontLoader;
import cn.feng.aluminium.util.render.RoundedUtil;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class MusicScreen extends GuiScreen {
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RoundedUtil.drawRound(10, 10, 200, 200, 3, Color.BLACK);
        SkijaFontLoader.noto.bold().drawGlowString("网易云音乐", 10, 10, 30, Color.WHITE, false);
    }
}
