package cn.feng.aluminium.ui.music.gui.component.impl;

import cn.feng.aluminium.util.render.ColorUtil;
import cn.feng.aluminium.util.render.shader.ShaderUtil;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/9/17
 **/
public class Ripple {
    private final long startTime;
    private final float x;
    private final float y;
    private final float maxRadius;
    private boolean finished = false;

    public boolean isFinished() {
        return finished;
    }

    public Ripple(long startTime, float x, float y, float maxRadius) {
        this.startTime = startTime;
        this.x = x;
        this.y = y;
        this.maxRadius = maxRadius;
    }

    public void render() {
        float percent = (float) ((float) (System.currentTimeMillis() - startTime) * 0.003);
        float radius = percent * maxRadius;
        float alpha = (1f - percent) * 0.7f;
        if (percent >= 1f) {
            finished = true;
            return;
        }
        ShaderUtil.drawCircle(x, y, radius, ColorUtil.applyOpacity(Color.WHITE, alpha));
    }
}
