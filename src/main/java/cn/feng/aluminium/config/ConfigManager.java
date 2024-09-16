package cn.feng.aluminium.config;

import cn.feng.aluminium.util.Util;

import java.io.File;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class ConfigManager extends Util {
    public static final File rootDir = new File(mc.mcDataDir, "Aluminium");
    public static final File cacheDir = new File(rootDir, "cache");
    public ConfigManager() {
        if (!rootDir.exists()) rootDir.mkdir();
        if (!cacheDir.exists()) cacheDir.mkdir();
    }
}
