package cn.feng.aluminium.music;

import cn.feng.aluminium.ui.nano.NanoUtil;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class MusicScreen extends GuiScreen {
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        NanoUtil.beginFrame();
        NanoUtil.drawRoundedRect(10, 10, 100, 100, 3f, Color.BLACK);
        NanoUtil.endFrame();
    }
}
