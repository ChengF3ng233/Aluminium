package cn.feng.aluminium.ui.music.gui.component.impl.player;

import cn.feng.aluminium.ui.music.gui.component.Component;
import cn.feng.aluminium.util.animation.advanced.Animation;
import cn.feng.aluminium.util.animation.advanced.Direction;
import cn.feng.aluminium.util.animation.advanced.impl.EaseOutCubic;
import cn.feng.aluminium.util.render.RenderUtil;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.Display;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;

public class IconButton extends Component {
    protected ResourceLocation currentIcon;
    private Runnable action;
    private Animation scaleAnim = new EaseOutCubic(200, 0.05, Direction.BACKWARDS);
    private boolean available = true;
    private boolean cursorRestored;

    public IconButton(ResourceLocation currentIcon, Runnable action) {
        this.currentIcon = currentIcon;
        this.action = action;
    }

    public IconButton(ResourceLocation currentIcon) {
        this.currentIcon = currentIcon;
    }

    public Runnable getAction() {
        return action;
    }

    public void setAction(Runnable action) {
        this.action = action;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public void update(float x, float y, float width, float height, int mouseX, int mouseY) {
        super.update(x, y, width, height, mouseX, mouseY);
        if (hovering && available) {
            if (scaleAnim.getDirection().backwards())
                scaleAnim.changeDirection();
            GLFW.glfwSetCursor(Display.getWindow(), GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR));
            cursorRestored = false;
        } else if (!hovering && !cursorRestored) {
            if (scaleAnim.getDirection().forwards())
                scaleAnim.changeDirection();
            GLFW.glfwSetCursor(Display.getWindow(), MemoryUtil.NULL);
            cursorRestored = true;
        }
    }

    @Override
    public void render() {
        RenderUtil.scaleStart(x + width / 2f, y + height / 2f, 1f + scaleAnim.getOutput().floatValue());
        RenderUtil.drawImage(currentIcon, x, y, width, height, available? Color.WHITE : new Color(200, 200, 200, 200));
        RenderUtil.scaleEnd();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (hovering) action.run();
    }
}
