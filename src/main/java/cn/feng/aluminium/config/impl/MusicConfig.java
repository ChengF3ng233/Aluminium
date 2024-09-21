package cn.feng.aluminium.config.impl;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.config.Config;
import cn.feng.aluminium.ui.music.api.MusicApi;
import cn.feng.aluminium.ui.music.api.bean.User;
import cn.feng.aluminium.util.data.DataUtil;
import cn.feng.aluminium.util.data.HttpUtil;
import com.google.gson.JsonObject;

/**
 * @author ChengFeng
 * @since 2024/8/12
 **/
public class MusicConfig extends Config {
    public MusicConfig() {
        super("music", "Music.json");
    }

    @Override
    public void loadConfig(JsonObject object) {
        Aluminium.INSTANCE.musicManager.setUser(DataUtil.gson.fromJson(object, User.class));
        User user = Aluminium.INSTANCE.musicManager.getUser();
        if (user.getId() != 0) {
            MusicApi.updateUserInfo();
        }
    }

    @Override
    public JsonObject saveConfig() {
        return DataUtil.gson.fromJson(DataUtil.gson.toJson(Aluminium.INSTANCE.musicManager.getUser()), JsonObject.class);
    }
}
