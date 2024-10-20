package cn.feng.aluminium.ui.music.gui;

import cn.feng.aluminium.ui.music.Theme;
import cn.feng.aluminium.ui.music.gui.component.impl.NavigationComponent;
import cn.feng.aluminium.ui.music.gui.component.impl.SearchComponent;
import cn.feng.aluminium.ui.music.gui.component.impl.category.CategorySidebar;
import cn.feng.aluminium.ui.music.gui.component.impl.player.PlayerComponent;
import cn.feng.aluminium.ui.music.gui.page.Page;
import cn.feng.aluminium.ui.music.gui.page.Pages;
import cn.feng.aluminium.util.render.RenderUtil;
import cn.feng.aluminium.util.render.shader.ShaderUtil;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class MusicScreen extends GuiScreen {
    private final float topHeight = 30f;
    private final float width = 500f;
    private final float height = 340f;
    // 组件
    private final CategorySidebar categorySidebar = new CategorySidebar();
    private final PlayerComponent playerComponent = new PlayerComponent();
    private final NavigationComponent navigationComponent = new NavigationComponent();
    private final SearchComponent searchComponent = new SearchComponent();
    // 坐标
    private float x = 20f, y = 20f;
    // 拖动
    private boolean isDragging = false;
    private int lastMouseX, lastMouseY;
    // 页面
    private Page currentPage = Pages.recommendPage;

    public void changePage(Page newPage) {
        if (currentPage.getChild() != newPage) {
            newPage.setParent(currentPage);
        }
        if (newPage.getChild() != currentPage) {
            currentPage.setChild(newPage);
        }
        currentPage = newPage;
    }

    public Page getCurrentPage() {
        return currentPage;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // 处理拖动
        if (isDragging) {
            x += mouseX - lastMouseX;
            y += mouseY - lastMouseY;
            lastMouseX = mouseX;
            lastMouseY = mouseY;
        }

        // 窗口背景
        ShaderUtil.drawGradientVertical(x, y, width, height, 5f, Theme.windowTop, Theme.windowBottom);

        // 更新组件坐标
        categorySidebar.update(x, y + topHeight, 40, height - topHeight, mouseX, mouseY);
        currentPage.update(x + 45f, y + topHeight + 5f, width - 50f, height - topHeight - 10f, mouseX, mouseY);
        playerComponent.update(x + width / 2f - 175f, y + height - 50f, 350f, 35f, mouseX, mouseY);
        navigationComponent.update(x + 10f, y + 10f, 45f, 18f, mouseX, mouseY);
        searchComponent.update(x + 50f, y + 10f, 85f, 13f, mouseX, mouseY);

        // 避免叠加交互
        if (categorySidebar.isHovering()) {
            playerComponent.setHovering(false);
            currentPage.disableHovering();
        }

        if (playerComponent.isHovering()) {
            currentPage.disableHovering();
        }

        // 绘制当前页面
        currentPage.render();
        currentPage.handleScroll();

        // 绘制组件
        playerComponent.render();
        categorySidebar.render();
        navigationComponent.render();
        searchComponent.render();
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (isDragging) isDragging = false;

        // 组件
        categorySidebar.mouseReleased(mouseX, mouseY, state);
        playerComponent.mouseReleased(mouseX, mouseY, state);

        // 当前页面
        currentPage.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (RenderUtil.hovering(mouseX, mouseY, x, y, width, topHeight) && mouseButton == 0) {
            isDragging = true;
            lastMouseX = mouseX;
            lastMouseY = mouseY;
        }

        // 组件
        categorySidebar.mouseClicked(mouseX, mouseY, mouseButton);
        playerComponent.mouseClicked(mouseX, mouseY, mouseButton);
        navigationComponent.mouseClicked(mouseX, mouseY, mouseButton);

        // 当前页面
        currentPage.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
