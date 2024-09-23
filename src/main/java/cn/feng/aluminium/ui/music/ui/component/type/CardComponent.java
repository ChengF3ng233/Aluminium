package cn.feng.aluminium.ui.music.ui.component.type;

import cn.feng.aluminium.ui.music.ui.component.AbstractComponent;
import cn.feng.aluminium.util.animation.advanced.Animation;
import cn.feng.aluminium.util.animation.advanced.Direction;
import cn.feng.aluminium.util.animation.advanced.impl.EaseBackIn;
import cn.feng.aluminium.util.animation.advanced.impl.EaseOutCubic;
import cn.feng.aluminium.util.render.RenderUtil;
import org.lwjgl.opengl.GL11;

public abstract class CardComponent extends AbstractComponent {
    protected final Animation translateAnimation = new EaseOutCubic(300, 3f, Direction.BACKWARDS);
    protected final Animation scaleAnimation = new EaseBackIn(300, 0.03f, 2f, Direction.BACKWARDS);

    protected void animate() {
        GL11.glPushMatrix();
        if ((hovering && !holding) && translateAnimation.getDirection().backwards()) {
            translateAnimation.changeDirection();
        } else if ((!hovering && !holding) && translateAnimation.getDirection().forwards())
            translateAnimation.changeDirection();
        if (holding && scaleAnimation.getDirection().backwards()) {
            scaleAnimation.changeDirection();
        } else if (!holding && scaleAnimation.getDirection().forwards()) {
            scaleAnimation.changeDirection();
        }

        GL11.glTranslated(-translateAnimation.getOutput(), -translateAnimation.getOutput(), 0d);
        RenderUtil.scaleStart(renderX, renderY, 1f - scaleAnimation.getOutput().floatValue());
    }

    protected void end() {
        RenderUtil.scaleEnd();
        GL11.glPopMatrix();
    }
}
