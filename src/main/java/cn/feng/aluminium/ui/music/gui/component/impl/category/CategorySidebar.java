package cn.feng.aluminium.ui.music.gui.component.impl.category;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.music.Theme;
import cn.feng.aluminium.ui.music.gui.MusicScreen;
import cn.feng.aluminium.ui.music.gui.component.Component;
import cn.feng.aluminium.ui.music.gui.page.Pages;
import cn.feng.aluminium.util.animation.advanced.Animation;
import cn.feng.aluminium.util.animation.advanced.Direction;
import cn.feng.aluminium.util.animation.advanced.composed.ColorAnimation;
import cn.feng.aluminium.util.animation.advanced.impl.DecelerateAnimation;
import cn.feng.aluminium.util.data.ResourceType;
import cn.feng.aluminium.util.data.ResourceUtil;
import cn.feng.aluminium.util.render.ColorUtil;
import cn.feng.aluminium.util.render.RenderUtil;
import cn.feng.aluminium.util.render.blur.BlurUtil;
import cn.feng.aluminium.util.render.shader.ShaderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CategorySidebar extends Component {
    private final List<CategoryButton> buttons = new ArrayList<>();
    private final Animation expandAnimation;
    private final ColorAnimation backgroundColor;

    public CategorySidebar() {
        buttons.add(new CategoryButton(ResourceUtil.getResource("home.png", ResourceType.ICON), "个性推荐", () -> Aluminium.INSTANCE.musicManager.getScreen().changePage(Pages.recommendPage)));
        buttons.add(new CategoryButton(ResourceUtil.getResource("person.png", ResourceType.ICON), "专属定制", () -> Aluminium.INSTANCE.musicManager.getScreen().changePage(Pages.recommendPage)));
        buttons.add(new CategoryButton(ResourceUtil.getResource("library_music.png", ResourceType.ICON), "歌单", () -> Aluminium.INSTANCE.musicManager.getScreen().changePage(Pages.recommendPage)));
        buttons.add(new CategoryButton(ResourceUtil.getResource("favorite_fill.png", ResourceType.ICON), "我喜欢的音乐", () -> Aluminium.INSTANCE.musicManager.getScreen().changePage(Pages.recommendPage)));
        buttons.add(new CategoryButton(ResourceUtil.getResource("cloud.png", ResourceType.ICON), "我的音乐云盘", () -> Aluminium.INSTANCE.musicManager.getScreen().changePage(Pages.cloudPage)));
        buttons.add(new CategoryButton(ResourceUtil.getResource("person.png", ResourceType.ICON), "个人主页", () -> Aluminium.INSTANCE.musicManager.getScreen().changePage(Pages.userPage)));
        expandAnimation = new DecelerateAnimation(200, 1.0d, Direction.BACKWARDS);
        backgroundColor = new ColorAnimation(ColorUtil.TRANSPARENT_COLOR, ColorUtil.TRANSPARENT_COLOR, 200);
    }

    @Override
    public void update(float x, float y, float width, float height, int mouseX, int mouseY) {
        super.update(x, y, width, height, mouseX, mouseY);
        this.width = width + 60 * expandAnimation.getOutput().floatValue();
        this.hovering = RenderUtil.hovering(mouseX, mouseY, x, y, this.width, height);
        float buttonY = y + 10f;
        for (CategoryButton button : buttons) {
            button.update(x + 10f, buttonY, this.width - 20f, 20f, mouseX, mouseY);
            buttonY += 23f;
        }
    }

    @Override
    public void render() {
        if (hovering) {
            buttons.forEach(CategoryButton::expand);
            if (expandAnimation.getDirection().backwards()) {
                expandAnimation.changeDirection();
                backgroundColor.change(ColorUtil.applyOpacity(Theme.layerBackground, 0.8f));
            }
            BlurUtil.processStart();
            ShaderUtil.drawVaryingRound(x, y, width, height, 0f, 5f, 5f, 0f, Color.BLACK);
            BlurUtil.blurEnd(2, 2);
        } else {
            if (expandAnimation.getDirection().forwards()) {
                expandAnimation.changeDirection();
                backgroundColor.change(ColorUtil.TRANSPARENT_COLOR);
            }
            if (expandAnimation.finished(Direction.BACKWARDS)) {
                buttons.forEach(CategoryButton::fold);
            }
        }
        ShaderUtil.drawVaryingRound(x, y, width, height, 0f, 5f, 5f, 0f, backgroundColor.getOutput());
        buttons.forEach(CategoryButton::render);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        // 只在Sidebar区域内检测
        if (hovering) {
            buttons.forEach(it -> it.mouseClicked(mouseX, mouseY, mouseButton));
        }
    }
}
