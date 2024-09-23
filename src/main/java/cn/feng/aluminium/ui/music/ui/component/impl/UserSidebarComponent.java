package cn.feng.aluminium.ui.music.ui.component.impl;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.music.ui.component.type.SidebarComponent;
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
        iconList.add(new IconButtonComponent(ResourceUtil.getResource("logout.png", ResourceType.ICON), () -> ((UserPage) Pages.USER).logout()));
    }
}
