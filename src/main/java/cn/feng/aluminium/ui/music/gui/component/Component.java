package cn.feng.aluminium.ui.music.gui.component;

import cn.feng.aluminium.util.render.RenderUtil;

public abstract class Component {
    protected float x, y;
    protected float width, height;
    protected int mouseX, mouseY;
    protected boolean hovering;
    public void update(float x, float y, float width, float height, int mouseX, int mouseY) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.hovering = RenderUtil.hovering(mouseX, mouseY, x, y, width, height);
    }

    public boolean isHovering() {
        return hovering;
    }

    public void setHovering(boolean hovering) {
        this.hovering = hovering;
    }

    public float getY() {
        return y;
    }

    public float getHeight() {
        return height;
    }

    public abstract void render();
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }
}
