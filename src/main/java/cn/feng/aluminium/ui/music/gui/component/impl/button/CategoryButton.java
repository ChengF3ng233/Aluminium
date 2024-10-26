package cn.feng.aluminium.ui.music.gui.component.impl.button;

import cn.feng.aluminium.ui.font.FontManager;
import cn.feng.aluminium.ui.music.gui.component.Component;
import cn.feng.aluminium.util.animation.advanced.Animation;
import cn.feng.aluminium.util.animation.advanced.Direction;
import cn.feng.aluminium.util.animation.advanced.composed.ColorAnimation;
import cn.feng.aluminium.util.animation.advanced.impl.EaseOutCubic;
import cn.feng.aluminium.util.render.ColorUtil;
import cn.feng.aluminium.util.render.RenderUtil;
import cn.feng.aluminium.util.render.shader.ShaderUtil;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class CategoryButton extends Component {
    private final ResourceLocation iconLocation;
    private final String text;
    private final Animation textAnim = new EaseOutCubic(200, 1.0, Direction.BACKWARDS);
    private final ColorAnimation backgroundColor = new ColorAnimation(ColorUtil.TRANSPARENT_COLOR, ColorUtil.TRANSPARENT_COLOR, 300);
    private boolean selected, expanded;
    private final Runnable action;

    public CategoryButton(ResourceLocation iconLocation, String text, Runnable action) {
        this.iconLocation = iconLocation;
        this.text = text;
        this.action = action;
    }

    @Override
    public void render() {
        if (selected || hovering) {
            backgroundColor.change(new Color(255, 255, 255, 20));
        } else {
            backgroundColor.change(new Color(255, 255, 255, 0));
        }

        ShaderUtil.drawRound(x, y, width, height, 2f, backgroundColor.getOutput());
        RenderUtil.drawImage(iconLocation, x + 5f, y + 5f, height - 10f, height - 10f);

        RenderUtil.scissorStart(x, y, width, height);
        FontManager.notoBold(15).drawCenteredStringV(text, x + (height - 10f) + 15f + textAnim.getOutput().floatValue() * 3f, y + height / 2f, Color.WHITE.getRGB());
        RenderUtil.scissorEnd();
    }

    public void expand() {
        expanded = true;
        if (textAnim.getDirection().forwards())
            textAnim.changeDirection();
    }

    public void fold() {
        expanded = false;
        if (textAnim.getDirection().backwards())
            textAnim.changeDirection();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (hovering && mouseButton == 0) {
            selected = true;
            action.run();
        } else if (!hovering) selected = false;
    }
}
