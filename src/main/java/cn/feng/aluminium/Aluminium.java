package cn.feng.aluminium;

import cn.feng.aluminium.config.ConfigManager;
import cn.feng.aluminium.event.EventManager;
import cn.feng.aluminium.module.ModuleManager;
import cn.feng.aluminium.ui.UIManager;
import cn.feng.aluminium.ui.music.MusicManager;
import cn.feng.aluminium.util.render.shader.ShaderUtil;

/**
 * @author ChengFeng
 * @since 2024/9/15
 **/
public class Aluminium {
    public static final Aluminium INSTANCE = new Aluminium();

    public EventManager eventManager;
    public ModuleManager moduleManager;
    public ConfigManager configManager;
    public UIManager uiManager;
    public MusicManager musicManager;

    public void init() {
        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        configManager = new ConfigManager();
        uiManager = new UIManager();
        musicManager = new MusicManager();

        configManager.init();
        moduleManager.init();
        musicManager.init();
        configManager.loadConfigs();

        ShaderUtil.init();
    }

    public void stop() {
        configManager.saveConfigs();
    }
}
