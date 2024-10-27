package cn.feng.aluminium.ui.music.gui.page;

import cn.feng.aluminium.ui.Scrollable;
import cn.feng.aluminium.util.animation.advanced.composed.CustomAnimation;
import cn.feng.aluminium.util.animation.advanced.impl.EaseOutCubic;
import org.lwjgl.opengl.GL11;

public abstract class Page extends Scrollable {
    protected float scrolledY;
    protected final CustomAnimation switchAnim = new CustomAnimation(EaseOutCubic.class, 250, 0, 0);
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

    public void onClose(boolean reverse) {
        switchAnim.setEndPoint(reverse? -width : width, true);
    }

    public void onOpen(boolean reverse) {
        switchAnim.setStartPoint(reverse? width : -width);
        switchAnim.setEndPoint(0, true);
    }

    protected void preRender() {
        GL11.glPushMatrix();
        if (parent != null && switchAnim.getEndPoint() == 0d) {
            if (switchAnim.getOutput() < 0d) parent.render();
        }
        if (child != null && switchAnim.getEndPoint() == 0d) {
            if (switchAnim.getOutput() > 0d) child.render();
        }
        GL11.glTranslated(switchAnim.getOutput(), 0d, 0d);
    }

    protected void postRender() {
        GL11.glPopMatrix();
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
