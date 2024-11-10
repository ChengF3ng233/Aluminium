package cn.feng.aluminium.ui.music.api.bean.lyric;

import cn.feng.aluminium.ui.nanovg.NanoUtil;
import cn.feng.aluminium.util.animation.advanced.Animation;
import cn.feng.aluminium.util.animation.advanced.Direction;
import cn.feng.aluminium.util.animation.advanced.composed.ColorAnimation;
import cn.feng.aluminium.util.animation.advanced.impl.EaseOutCubic;

import java.awt.*;

public class LyricLeader extends LyricLine {
    private final ColorAnimation circle1 = new ColorAnimation(Color.WHITE, new Color(200, 200, 200, 200), 250);
    private final ColorAnimation circle2 = new ColorAnimation(Color.WHITE, new Color(200, 200, 200, 200), 250);
    private final ColorAnimation circle3 = new ColorAnimation(Color.WHITE, new Color(200, 200, 200, 200), 250);
    private final Animation scale = new EaseOutCubic(300, 1.0, Direction.BACKWARDS);

    public LyricLeader(int startTime, int duration) {
        super("[前奏]", startTime, duration);
    }

    @Override
    public void render(float time, int currentIndex) {
        if (match(time)) {
            float percent = (time - startTime) / duration;
            if (percent >= 0.3f && circle1.getDirection().forwards()) circle1.changeDirection();
            if (percent >= 0.6f && circle2.getDirection().forwards()) circle2.changeDirection();
            if (percent >= 0.9f && circle3.getDirection().forwards()) circle3.changeDirection();
            if (scale.getDirection().forwards()) scale.changeDirection();
        } else {
            if (circle1.getDirection().backwards()) circle1.changeDirection();
            if (circle2.getDirection().backwards()) circle2.changeDirection();
            if (circle3.getDirection().backwards()) circle3.changeDirection();
            if (scale.getDirection().backwards()) scale.changeDirection();
        }
        NanoUtil.scaleStart(originX + 9f, originY - scrollAnim.getOutput().floatValue(), 1f - scale.getOutput().floatValue());
        NanoUtil.drawCircle(originX + 3f, originY - scrollAnim.getOutput().floatValue(), 3f, circle1.getOutput());
        NanoUtil.drawCircle(originX + 12f, originY - scrollAnim.getOutput().floatValue(), 3f, circle2.getOutput());
        NanoUtil.drawCircle(originX + 21f, originY - scrollAnim.getOutput().floatValue(), 3f, circle3.getOutput());
        NanoUtil.scaleEnd();
    }
}
