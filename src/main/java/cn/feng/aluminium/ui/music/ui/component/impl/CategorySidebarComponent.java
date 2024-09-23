package cn.feng.aluminium.ui.music.ui.component.impl;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.music.ui.component.type.SidebarComponent;
import cn.feng.aluminium.ui.music.ui.page.Pages;
import cn.feng.aluminium.util.data.resource.ResourceType;
import cn.feng.aluminium.util.data.resource.ResourceUtil;
import cn.feng.aluminium.util.misc.ChatUtil;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class CategorySidebarComponent extends SidebarComponent {
    public CategorySidebarComponent() {
        iconList.add(new IconButtonComponent(ResourceUtil.getResource("home.png", ResourceType.ICON), () -> Aluminium.INSTANCE.musicManager.getScreen().setCurrentPage(Pages.HOME), true));
        iconList.add(new IconButtonComponent(ResourceUtil.getResource("library_music.png", ResourceType.ICON), () -> ChatUtil.sendMessage("Library"), true));
        iconList.add(new IconButtonComponent(ResourceUtil.getResource("favorite.png", ResourceType.ICON), () -> ChatUtil.sendMessage("Favorite"), true));
        iconList.add(new IconButtonComponent(ResourceUtil.getResource("cloud.png", ResourceType.ICON), () -> ChatUtil.sendMessage("Cloud"), true));
    }
}
