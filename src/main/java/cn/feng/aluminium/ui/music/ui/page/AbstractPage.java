package cn.feng.aluminium.ui.music.ui.page;

import cn.feng.aluminium.util.animation.advanced.Direction;
import cn.feng.aluminium.util.animation.advanced.composed.CustomAnimation;
import cn.feng.aluminium.util.animation.advanced.impl.EaseOutCubic;
import cn.feng.aluminium.util.render.RenderUtil;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public abstract class AbstractPage {
    protected float renderX, renderY, width, height, centerX, centerY, scale, scrolledY;
    protected float pageHeight;
    protected boolean hovering = false;
    protected CustomAnimation scrollAnimation = new CustomAnimation(EaseOutCubic.class, 120, 0d, 0d);
    private CustomAnimation switchAnimation = new CustomAnimation(EaseOutCubic.class, 300, 0d, 0d);
    protected AbstractPage parent;

    public void setParent(AbstractPage parent) {
        this.parent = parent;
    }

    public abstract void render();

    protected float getMaxScroll() {
        return Math.max(pageHeight - height, 0f);
    }

    protected void reachBottom() {

    }

    public void update(float renderX, float renderY, float width, float height, int mouseX, int mouseY, float centerX, float centerY, float scale) {
        this.renderX = renderX;
        this.renderY = renderY;
        this.width = width;
        this.height = height;
        this.hovering = RenderUtil.hovering(mouseX, mouseY, renderX, renderY, width, height);
        this.centerX = centerX;
        this.centerY = centerY;
        this.scale = scale;
        scrolledY = renderY + scrollAnimation.getOutput().floatValue();
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

    public void onClose() {
        switchAnimation.setEndPoint(width, true);
    }

    public void onOpen() {
        switchAnimation.setStartPoint(-width);
        switchAnimation.setEndPoint(0d);
        switchAnimation.getAnimation().reset();
    }

    protected void preDraw() {
        GL11.glPushMatrix();
        if (parent != null && switchAnimation.getEndPoint() == 0d && !switchAnimation.getAnimation().finished(Direction.FORWARDS)) {
            parent.render();
        }
        RenderUtil.scissorStart(renderX, renderY, width, height, centerX, centerY, scale);
        GL11.glTranslated(switchAnimation.getOutput(), 0d, 0d);
    }

    protected void postDraw() {
        RenderUtil.scissorEnd();
        GL11.glPopMatrix();
    }

    public void mouseClicked(int mouseButton) {

    }

    public void mouseReleased() {

    }

    public void keyTyped(char c, int keyCode) {

    }
}
