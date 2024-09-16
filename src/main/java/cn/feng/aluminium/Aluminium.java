package cn.feng.aluminium;

import cn.feng.aluminium.config.ConfigManager;
import cn.feng.aluminium.event.EventManager;
import cn.feng.aluminium.module.ModuleManager;
import cn.feng.aluminium.ui.UIManager;
import cn.feng.aluminium.ui.font.SkijaFontLoader;
import cn.feng.aluminium.ui.music.MusicManager;

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

        moduleManager.init();
        configManager.init();
        SkijaFontLoader.init();
        musicManager.init();

        configManager.loadConfigs();
    }

    public void stop() {
        configManager.saveConfigs();
    }
}
