package cn.feng.aluminium.ui.menu;

import cn.feng.aluminium.module.modules.visual.hud.HUD;
import cn.feng.aluminium.ui.Movable;
import cn.feng.aluminium.ui.nanovg.NanoFontLoader;
import cn.feng.aluminium.ui.nanovg.NanoFontRenderer;
import cn.feng.aluminium.ui.nanovg.NanoUtil;
import cn.feng.aluminium.util.animation.advanced.composed.ColorAnimation;
import org.lwjgl.nanovg.NanoVG;

import java.awt.*;

public class MainMenuButton extends Movable {
    private final String text;
    private final ButtonMode mode;
    private final Runnable action;
    private final ColorAnimation color = new ColorAnimation(new Color(200, 200, 200, 0), new Color(200, 200, 200, 0), 300);

    public MainMenuButton(String text, ButtonMode mode, Runnable action) {
        this.text = text;
        this.mode = mode;
        this.action = action;
    }

    public void render(float radius) {
        switch (mode) {
            case TOP: {
                NanoUtil.drawRoundedRect(x, y, width, height, radius, radius, 0f, 0f, color.getOutput());
                break;
            }

            case BOTTOM: {
                NanoUtil.drawRoundedRect(x, y, width, height, 0f, 0f, radius, radius, color.getOutput());
                break;
            }

            default: {
                NanoUtil.drawRect(x, y, width, height, color.getOutput());
            }
        }
        NanoFontRenderer font = NanoFontLoader.noto.bold();
        font.setSpacing(0.5f);
        font.drawString(text, x + width / 2f, y + height / 2f, 15f, NanoVG.NVG_ALIGN_CENTER | NanoVG.NVG_ALIGN_MIDDLE, HUD.getAccentColor());
    }

    @Override
    public void update(float x, float y, float width, float height, int mouseX, int mouseY) {
        super.update(x, y, width, height, mouseX, mouseY);
        if (hovering) {
            color.change(new Color(200, 200, 200, 100));
        } else color.change(new Color(200, 200, 200, 0));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (hovering && mouseButton == 0) {
            action.run();
        }
    }
}
