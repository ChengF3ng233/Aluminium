package cn.feng.aluminium.ui.music.gui.page;

import cn.feng.aluminium.util.animation.advanced.composed.CustomAnimation;
import cn.feng.aluminium.util.animation.advanced.impl.EaseOutCubic;
import cn.feng.aluminium.util.render.RenderUtil;
import org.lwjgl.input.Mouse;

public abstract class Page {
    protected float x, y, scrolledY;
    protected float width, height;
    protected boolean hovering;
    protected float pageHeight;
    protected CustomAnimation scrollAnimation = new CustomAnimation(EaseOutCubic.class, 120, 0d, 0d);
    protected Page parent;
    protected Page child;

    public Page getChild() {
        return child;
    }

    public void setChild(Page child) {
        this.child = child;
    }

    public void setParent(Page parent) {
        this.parent = parent;
    }

    public Page getParent() {
        return parent;
    }

    public void update(float x, float y, float width, float height, int mouseX, int mouseY) {
        this.x = x;
        this.y = y;
        scrolledY = y + scrollAnimation.getOutput().floatValue();
        this.width = width;
        this.height = height;
        this.hovering = RenderUtil.hovering(mouseX, mouseY, x, y, width, height);
    }

    public abstract void render();

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    protected float getMaxScroll() {
        return Math.max(pageHeight - height, 0f);
    }

    protected void reachBottom() {

    }

    public void disableHovering() {
        hovering = false;
    }

    public void handleScroll() {
        int wheel = Mouse.getDWheel();
        if (wheel > 0) {
            scrollAnimation.setEndPoint(scrollAnimation.getEndPoint() + 30f, true);
        } else if (wheel < 0) {
            scrollAnimation.setEndPoint(scrollAnimation.getEndPoint() - 30f, true);
        }
        if (scrollAnimation.getEndPoint() > 0f) {
            scrollAnimation.setEndPoint(0f);
        }
        if (scrollAnimation.getEndPoint() < -getMaxScroll()) {
            scrollAnimation.setEndPoint(-getMaxScroll());
            reachBottom();
        }
    }
}
