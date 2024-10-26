package cn.feng.aluminium.ui.music.gui.page;

import cn.feng.aluminium.ui.Scrollable;

public abstract class Page extends Scrollable {
    protected float scrolledY;
    protected Page parent;
    protected Page child;

    public Page getChild() {
        return child;
    }

    public void setChild(Page child) {
        this.child = child;
    }

    public Page getParent() {
        return parent;
    }

    public void setParent(Page parent) {
        this.parent = parent;
    }

    @Override
    public void update(float x, float y, float width, float height, int mouseX, int mouseY) {
        super.update(x, y, width, height, mouseX, mouseY);
        scrolledY = y + scrollAnimation.getOutput().floatValue();
    }

    public void disableHovering() {
        hovering = false;
    }

    public abstract void render();
}
