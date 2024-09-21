package cn.feng.aluminium.ui.music.ui.component.impl;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.music.ui.page.Pages;
import cn.feng.aluminium.ui.music.ui.page.impl.UserPage;
import cn.feng.aluminium.util.data.resource.ResourceType;
import cn.feng.aluminium.util.data.resource.ResourceUtil;

/**
 * @author ChengFeng
 * @since 2024/9/17
 **/
public class UserSidebarComponent extends SidebarComponent {
    public UserSidebarComponent() {
        iconList.add(new IconButtonComponent(ResourceUtil.getResource("person.png", ResourceType.ICON), () -> Aluminium.INSTANCE.musicManager.getScreen().setCurrentPage(Pages.USER), true));

        SwitchButtonComponent skinButton = new SwitchButtonComponent(
                ResourceUtil.getResource("dark_mode.png", ResourceType.ICON),
                ResourceUtil.getResource("dark_mode.png", ResourceType.ICON),
                ResourceUtil.getResource("light_mode.png", ResourceType.ICON)
        );

        skinButton.setAction(() -> {
            Aluminium.INSTANCE.musicManager.getScreen().setDark(skinButton.getIcons().indexOf(skinButton.getIconLocation()) == 0);
        });

        iconList.add(skinButton);

        iconList.add(new IconButtonComponent(ResourceUtil.getResource("logout.png", ResourceType.ICON), () -> ((UserPage) Pages.USER).logout()));
    }
}
