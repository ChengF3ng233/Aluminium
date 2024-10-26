package cn.feng.aluminium.ui.music.gui.component.impl.composed;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.music.gui.MusicScreen;
import cn.feng.aluminium.ui.music.gui.component.Component;
import cn.feng.aluminium.ui.music.gui.component.impl.button.IconButton;
import cn.feng.aluminium.util.data.ResourceType;
import cn.feng.aluminium.util.data.ResourceUtil;

public class NavigationComponent extends Component {
    private final IconButton back;
    private final IconButton forward;

    public NavigationComponent() {
        back = new IconButton(ResourceUtil.getResource("arrow_left.png", ResourceType.ICON));
        forward = new IconButton(ResourceUtil.getResource("arrow_right.png", ResourceType.ICON));

        back.setAction(() -> {
            MusicScreen screen = Aluminium.INSTANCE.musicManager.getScreen();
            if (screen.getCurrentPage().getParent() != null) {
                screen.changePage(screen.getCurrentPage().getParent());
                back.setAvailable(screen.getCurrentPage().getParent() != null);
                forward.setAvailable(screen.getCurrentPage().getChild() != null);
            }
        });

        forward.setAction(() -> {
            MusicScreen screen = Aluminium.INSTANCE.musicManager.getScreen();
            if (screen.getCurrentPage().getChild() != null) {
                screen.changePage(screen.getCurrentPage().getChild());

                back.setAvailable(screen.getCurrentPage().getParent() != null);
                forward.setAvailable(screen.getCurrentPage().getChild() != null);
            }
        });
    }

    public void onChangePage() {
        MusicScreen screen = Aluminium.INSTANCE.musicManager.getScreen();
        back.setAvailable(screen.getCurrentPage().getParent() != null);
        forward.setAvailable(screen.getCurrentPage().getChild() != null);
    }

    @Override
    public void update(float x, float y, float width, float height, int mouseX, int mouseY) {
        super.update(x, y, width, height, mouseX, mouseY);
        back.update(x, y, height, height, mouseX, mouseY);
        forward.update(x + (width - height * 2f), y, height, height, mouseX, mouseY);
    }

    @Override
    public void render() {
        back.render();
        forward.render();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        back.mouseClicked(mouseX, mouseY, mouseButton);
        forward.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
