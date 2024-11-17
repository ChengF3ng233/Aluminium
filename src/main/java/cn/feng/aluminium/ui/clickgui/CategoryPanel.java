package cn.feng.aluminium.ui.clickgui;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.module.Module;
import cn.feng.aluminium.module.ModuleCategory;
import cn.feng.aluminium.ui.Scrollable;
import cn.feng.aluminium.ui.clickgui.component.ModuleButton;
import cn.feng.aluminium.ui.nanovg.NanoFontLoader;
import cn.feng.aluminium.ui.nanovg.NanoUtil;
import cn.feng.aluminium.util.animation.advanced.composed.CustomAnimation;
import cn.feng.aluminium.util.animation.advanced.impl.EaseOutCubic;
import cn.feng.aluminium.util.data.StringUtil;
import cn.feng.aluminium.util.render.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryPanel extends Scrollable {
    private final ClickGui parent;

    private final List<ModuleButton> buttons = new ArrayList<>();
    private final ModuleCategory category;

    private final float maxHeight = 250f;
    private final float minHeight;
    private final CustomAnimation heightAnim = new CustomAnimation(EaseOutCubic.class, 300, 0d, 0d);
    private float totalHeight;

    public CategoryPanel(ClickGui parent, ModuleCategory category) {
        this.parent = parent;
        this.category = category;

        for (Module module : Aluminium.INSTANCE.moduleManager.getModuleByCategory(category)) {
            buttons.add(new ModuleButton(this, module));
        }

        totalHeight = (buttons.size() + 1) * 20f + 3f;
        minHeight = Math.min(maxHeight, totalHeight);
        heightAnim.setEndPoint(minHeight, true);
    }

    @Override
    public void update(float x, float y, float width, float height, int mouseX, int mouseY) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = heightAnim.getOutput().floatValue();
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.hovering = RenderUtil.hovering(mouseX, mouseY, x, y, width, this.height);
        this.maxScroll = Math.max(totalHeight - this.height, 0);

        float buttonY = y + scrollAnimation.getOutput().floatValue() + 17f;
        final float buttonHeight = 20f;
        for (ModuleButton button : buttons) {
            button.update(x + 1f, buttonY, width - 2f, buttonHeight, mouseX, mouseY);
            buttonY += buttonHeight;
        }

        if (this.hovering) handleScroll();
    }

    public void applyExpand(float height) {
        heightAnim.setEndPoint(Math.min(maxHeight, heightAnim.getEndPoint() + height), true);
        totalHeight += height;
    }

    public void applyFold(float height) {
        heightAnim.setEndPoint(Math.max(minHeight, heightAnim.getEndPoint() - height), true);
        totalHeight -= height;
    }

    public void render() {
        NanoUtil.drawRoundedRect(x, y, width, height, 5f, new Color(30, 30, 30));
        NanoFontLoader.quicksand.bold().drawString(StringUtil.capitalizeFirstLetter(category.name()), x + 10f, y + 4f, 16f, Color.WHITE);
        NanoUtil.scissorStart(x, y + 17f, width, height - 23f);
        for (ModuleButton button : buttons) {
            button.render();
        }
        NanoUtil.scissorEnd();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (hovering) {
            for (ModuleButton button : buttons) {
                button.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }
}
