package cn.feng.aluminium.ui.music.ui.component.impl;

import cn.feng.aluminium.ui.music.ui.Theme;
import cn.feng.aluminium.ui.music.ui.component.AbstractComponent;
import cn.feng.aluminium.util.data.resource.ResourceType;
import cn.feng.aluminium.util.data.resource.ResourceUtil;
import cn.feng.aluminium.util.misc.ChatUtil;
import cn.feng.aluminium.util.render.shader.ShaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class CategorySidebarComponent extends AbstractComponent {
    private final List<IconButtonComponent> iconList = new ArrayList<>();

    public CategorySidebarComponent() {
        iconList.add(new IconButtonComponent(ResourceUtil.getResource("home.png", ResourceType.ICON), () -> ChatUtil.sendMessage("Click")));
    }

    @Override
    public void render() {
        ShaderUtil.drawRound(renderX, renderY, width, height, width / 2f, Theme.darkAlt);
        iconList.forEach(IconButtonComponent::render);
    }

    @Override
    public void update(float renderX, float renderY, float width, float height, int mouseX, int mouseY) {

        height = 2f;
        for (IconButtonComponent icon : iconList) {
            icon.update(renderX + 3f, renderY, width - 6f, width - 6f, mouseX, mouseY);
            height += icon.getHeight() + 2f;
        }

        super.update(renderX, renderY, width, height, mouseX, mouseY);
    }

    @Override
    public void onMouseClicked(int mouseButton) {
        iconList.forEach(AbstractComponent::onMouseReleased);
    }
}
