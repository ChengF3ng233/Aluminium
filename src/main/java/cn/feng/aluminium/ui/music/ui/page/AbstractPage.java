package cn.feng.aluminium.ui.music.ui.page;

import cn.feng.aluminium.util.render.RenderUtil;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public abstract class AbstractPage {
    protected float renderX, renderY, width, height;
    protected boolean hovering = false;

    public abstract void render();

    public void update(float renderX, float renderY, float width, float height, int mouseX, int mouseY) {
        this.renderX = renderX;
        this.renderY = renderY;
        this.width = width;
        this.height = height;
        this.hovering = RenderUtil.hovering(mouseX, mouseY, renderX, renderY, width, height);
    }

    public void onMouseClicked(int mouseButton) {

    }

    public void onMouseReleased() {

    }

    public void keyTyped(char c, int keyCode) {

    }
}
