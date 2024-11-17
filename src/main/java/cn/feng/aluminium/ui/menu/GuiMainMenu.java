package cn.feng.aluminium.ui.menu;

import cn.feng.aluminium.module.modules.visual.hud.HUD;
import cn.feng.aluminium.ui.nanovg.NanoFontLoader;
import cn.feng.aluminium.ui.nanovg.NanoUtil;
import cn.feng.aluminium.util.data.ResourceType;
import cn.feng.aluminium.util.data.ResourceUtil;
import cn.feng.aluminium.util.render.ColorUtil;
import cn.feng.aluminium.util.render.RenderUtil;
import cn.feng.aluminium.util.render.blur.GaussianBlur;
import cn.feng.aluminium.util.render.shader.ShaderUtil;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import org.lwjgl.nanovg.NanoVG;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiMainMenu extends GuiScreen {
    private final List<MainMenuButton> buttons = new ArrayList<>();

    public GuiMainMenu() {
        buttons.add(new MainMenuButton("Single Player", ButtonMode.TOP, () -> mc.displayGuiScreen(new GuiSelectWorld(this))));
        buttons.add(new MainMenuButton("Multi Player", ButtonMode.MIDDLE, () -> mc.displayGuiScreen(new GuiMultiplayer(this))));
        buttons.add(new MainMenuButton("Alt Manager", ButtonMode.MIDDLE, () -> {}));
        buttons.add(new MainMenuButton("Options", ButtonMode.MIDDLE, () -> mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings))));
        buttons.add(new MainMenuButton("Shutdown", ButtonMode.BOTTOM, () -> mc.shutdown()));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        float centerX = width / 2f;
        float centerY = height / 2f;
        RenderUtil.drawImage(ResourceUtil.getResource("background.png", ResourceType.IMAGE), 0, 0, width, height);
        GaussianBlur.startBlur();
        ShaderUtil.drawRound(centerX - 70f, centerY - 60f, 140f, 165f, 7f, Color.BLACK);
        GaussianBlur.endBlur(20, 1);

        NanoUtil.beginFrame();
        NanoUtil.drawRect(0, 0, width, height, new Color(0, 0, 0, 100));
        NanoFontLoader.quicksand.bold().drawGlowString("Aluminium", centerX, centerY - 50f, 30f, 5f, NanoVG.NVG_ALIGN_CENTER | NanoVG.NVG_ALIGN_TOP, HUD.getAccentColor());
        NanoUtil.drawRoundedRect(centerX - 60f, centerY - 30f, 120f, 125f, 10f, new Color(50, 50, 50));
        float buttonY = centerY - 30f;
        final float buttonX = centerX - 60f;
        final float buttonWidth = 120f;
        final float buttonHeight = 25f;
        for (MainMenuButton button : buttons) {
            button.update(buttonX, buttonY, buttonWidth, buttonHeight, mouseX, mouseY);
            buttonY += buttonHeight;
        }
        for (MainMenuButton button : buttons) {
            button.render(10f);
        }
        NanoUtil.endFrame();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (MainMenuButton button : buttons) {
            button.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        // Disable escape to prevent reset
    }
}
