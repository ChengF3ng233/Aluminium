package cn.feng.aluminium.ui.music.ui.page;

import cn.feng.aluminium.ui.music.ui.page.impl.HomePage;
import cn.feng.aluminium.ui.music.ui.page.impl.PlaylistPage;
import cn.feng.aluminium.ui.music.ui.page.impl.UserPage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ChengFeng
 * @since 2024/9/17
 **/
public class Pages {
    public static final AbstractPage HOME = new HomePage();
    public static final AbstractPage USER = new UserPage();
    public static final Map<Long, PlaylistPage> playlistPageMap = new HashMap<>();
}
