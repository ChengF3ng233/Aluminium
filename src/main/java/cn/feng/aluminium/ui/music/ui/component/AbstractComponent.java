package cn.feng.aluminium.ui.music.ui.component;

import cn.feng.aluminium.util.render.RenderUtil;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public abstract class AbstractComponent {
    protected float renderX, renderY, width, height, mouseX, mouseY;
    protected boolean hovering = false;
    protected boolean holding = false;

    public abstract void render();

    public void update(float renderX, float renderY, float width, float height, int mouseX, int mouseY) {
        this.renderX = renderX;
        this.renderY = renderY;
        this.width = width;
        this.height = height;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.hovering = RenderUtil.hovering(mouseX, mouseY, renderX, renderY, width, height);
        this.holding = RenderUtil.holding(mouseX, mouseY, renderX, renderY, width, height, 0);
    }

    public void mouseClicked(int mouseButton) {

    }

    public void mouseReleased() {

    }

    public void keyTyped(char c, int keyCode) {

    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
