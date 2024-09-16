package cn.feng.aluminium.config;

import cn.feng.aluminium.config.impl.MusicConfig;
import cn.feng.aluminium.util.Util;
import cn.feng.aluminium.util.exception.MemberNotFoundException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/7/29
 **/
public class ConfigManager extends Util {
    public static final File rootDir = new File(mc.mcDataDir, "Aluminium");
    public static final File cacheDir = new File(rootDir, "cache");
    public static final File coverDir = new File(rootDir, "cover");
    public static final File musicDir = new File(rootDir, "music");
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final List<Config> configList;

    public ConfigManager() {
        configList = new ArrayList<>();
        if (!rootDir.exists()) rootDir.mkdir();
        if (!cacheDir.exists()) cacheDir.mkdir();
        if (!coverDir.exists()) coverDir.mkdir();
        if (!musicDir.exists()) musicDir.mkdir();
    }

    public Config getConfig(String name) {
        for (Config config : configList) {
            if (config.name.equals(name)) return config;
        }

        throw new MemberNotFoundException("Config not found: " + name);
    }

    public Config getConfig(Class<? extends Config> klass) {
        for (Config config : configList) {
            if (config.getClass() == klass) return config;
        }

        throw new MemberNotFoundException("Config not found: " + klass.getName());
    }

    public void saveConfig(Class<? extends Config> klass) {
        Config config = getConfig(klass);
        JsonObject object = config.saveConfig();
        try {
            PrintWriter pw = new PrintWriter(config.configFile);
            pw.write(ConfigManager.gson.toJson(object));
            pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {
            logger.error("Failed to write {} klass.", config.name);
        }
    }

    public void init() {
        configList.add(new MusicConfig());
    }

    public void loadConfigs() {
        for (Config config : configList) {
            if (!config.configFile.exists()) continue;
            try {
                JsonObject object = ConfigManager.gson.fromJson(new FileReader(config.configFile), JsonObject.class);
                config.loadConfig(object);
            } catch (FileNotFoundException e) {
                logger.error("Failed to load config {}", config.name);
            }
        }
    }

    public void saveConfigs() {
        configList.forEach(it -> {
            if (!it.configFile.exists()) {
                try {
                    it.configFile.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        for (Config config : configList) {
            JsonObject object = config.saveConfig();
            try {
                PrintWriter pw = new PrintWriter(config.configFile);
                pw.write(ConfigManager.gson.toJson(object));
                pw.flush();
                pw.close();
            } catch (FileNotFoundException e) {
                logger.error("Failed to write {} config.", config.name);
            }
        }
    }
}
