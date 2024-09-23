package cn.feng.aluminium.util.animation.advanced;


import cn.feng.aluminium.util.misc.TimerUtil;

/**
 * This animation superclass was made by Foggy and advanced by cedo
 *
 * @author Foggy
 * @author cedo
 * @since 7/21/2020 (yes 2020)
 * @since 7/29/2021
 */
public abstract class Animation {
    private final TimerUtil timerUtil = new TimerUtil();
    private int duration;
    private double endPoint;
    protected Direction direction;

    public Animation(int ms, double endPoint) {
        this(ms, endPoint, Direction.FORWARDS);
    }

    public Animation(int ms, double endPoint, Direction direction) {
        this.duration = ms; //Time in milliseconds of how long you want the animation to take.
        this.endPoint = endPoint; //The desired distance for the animated object to go.
        this.direction = direction; //Direction in which the graph is going. If backwards, will start from endPoint and go to 0.
    }

    public boolean finished(Direction direction) {
        return isDone() && this.direction.equals(direction);
    }

    public double getLinearOutput() {
        return 1 - ((timerUtil.getTime() / (double) duration) * endPoint);
    }

    public void reset() {
        timerUtil.reset();
    }

    public boolean isDone() {
        return timerUtil.hasTimeElapsed(duration);
    }

    public void changeDirection() {
        setDirection(direction.opposite());
    }

    public Animation setDirection(Direction direction) {
        if (this.direction != direction) {
            this.direction = direction;
            timerUtil.setTime(System.currentTimeMillis() - (duration - Math.min(timerUtil.getTime(), duration)));
        }
        return this;
    }

    protected boolean correctOutput() {
        return false;
    }

    public Double getOutput() {
        if (direction.forwards()) {
            if (isDone()) {
                return endPoint;
            }

            return getEquation(timerUtil.getTime() / (double) duration) * endPoint;
        } else {
            if (isDone()) {
                return 0.0;
            }

            double revTime = Math.min(duration, Math.max(0, duration - timerUtil.getTime()));
            return getEquation(revTime / (double) duration) * endPoint;
        }
    }


    //This is where the animation equation should go, for example, a logistic function. Output should range from 0 to 1.
    //This will take the timer's time as an input, x.
    protected abstract double getEquation(double x);

    public int getDuration() {
        return duration;
    }

    public double getEndPoint() {
        return endPoint;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setEndPoint(double endPoint) {
        this.endPoint = endPoint;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
