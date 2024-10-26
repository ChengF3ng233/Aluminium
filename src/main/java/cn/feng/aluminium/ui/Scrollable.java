package cn.feng.aluminium.ui;

import cn.feng.aluminium.util.animation.advanced.composed.CustomAnimation;
import cn.feng.aluminium.util.animation.advanced.impl.EaseOutCubic;
import org.lwjgl.input.Mouse;

public class Scrollable extends Movable {
    protected final CustomAnimation scrollAnimation = new CustomAnimation(EaseOutCubic.class, 120, 0d, 0d);
    protected float maxScroll;

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
        if (scrollAnimation.getEndPoint() < -maxScroll) {
            scrollAnimation.setEndPoint(-maxScroll);
            reachBottom();
        }
    }

    protected void reachBottom() {

    }
}
