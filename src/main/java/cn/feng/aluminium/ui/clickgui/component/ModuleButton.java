package cn.feng.aluminium.ui.clickgui.component;

import cn.feng.aluminium.module.Module;
import cn.feng.aluminium.module.modules.visual.hud.HUD;
import cn.feng.aluminium.ui.Movable;
import cn.feng.aluminium.ui.nanovg.NanoFontLoader;
import cn.feng.aluminium.ui.nanovg.NanoUtil;
import cn.feng.aluminium.util.animation.advanced.composed.ColorAnimation;
import org.lwjgl.nanovg.NanoVG;

import java.awt.*;

public class ModuleButton extends Movable {
    private final Module module;
    private final ColorAnimation bgColor = new ColorAnimation(new Color(50, 50, 50), new Color(50, 50, 50), 300);
    private final ColorAnimation textColor = new ColorAnimation(Color.WHITE, Color.WHITE, 300);

    public ModuleButton(Module module) {
        this.module = module;
    }

    public void render() {
        NanoUtil.drawRect(x, y, width, height, bgColor.getOutput());
        NanoFontLoader.noto.bold().drawString(module.getName(), x + 10f, y + height / 2f, 14f, NanoVG.NVG_ALIGN_LEFT | NanoVG.NVG_ALIGN_MIDDLE, textColor.getOutput());
    }

    @Override
    public void update(float x, float y, float width, float height, int mouseX, int mouseY) {
        super.update(x, y, width, height, mouseX, mouseY);
        if (hovering) {
            bgColor.change(module.isEnabled() ? HUD.getAccentColor().brighter() : new Color(70, 70, 70));
        } else {
            bgColor.change(module.isEnabled() ? HUD.getAccentColor() : new Color(50, 50, 50));
        }

        textColor.change(module.isEnabled() ? Color.BLACK : Color.WHITE);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (hovering && mouseButton == 0) {
            module.toggle();
        }
    }
}
