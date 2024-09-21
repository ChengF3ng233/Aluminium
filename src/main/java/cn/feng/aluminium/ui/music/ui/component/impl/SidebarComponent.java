package cn.feng.aluminium.ui.music.ui.component.impl;

import cn.feng.aluminium.ui.music.ui.component.AbstractComponent;
import cn.feng.aluminium.util.render.shader.ShaderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/9/17
 **/
public class SidebarComponent extends AbstractComponent {
    protected final List<IconButtonComponent> iconList = new ArrayList<>();
    private IconButtonComponent selectedButton;
    private Color backgroundColor;

    public IconButtonComponent getSelectedButton() {
        return selectedButton;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void render() {
        ShaderUtil.drawRound(renderX, renderY, width, height, width / 2f, backgroundColor);
        iconList.forEach(IconButtonComponent::render);
    }

    @Override
    public void update(float renderX, float renderY, float width, float height, int mouseX, int mouseY) {
        height = 4f;
        for (IconButtonComponent icon : iconList) {
            icon.update(renderX + 4f, renderY + height, width - 8f, width - 8f, mouseX, mouseY);
            height += icon.getHeight() + 3f;
        }
        height += 1f;

        super.update(renderX, renderY, width, height, mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseButton) {
        selectedButton = null;
        for (IconButtonComponent iconButtonComponent : iconList) {
            boolean previousState = iconButtonComponent.isSelected();
            iconButtonComponent.mouseClicked(mouseButton);
            // 新选中的按钮
            if (previousState != iconButtonComponent.isSelected() && iconButtonComponent.isSelected()) {
                selectedButton = iconButtonComponent;
                // 取第一个
                break;
            }
        }
    }

    public void updateButtons(IconButtonComponent selectedButton) {
        if (selectedButton == null) return;
        for (IconButtonComponent iconButtonComponent : iconList) {
            if (iconButtonComponent != selectedButton) iconButtonComponent.setSelected(false);
        }
    }
}
