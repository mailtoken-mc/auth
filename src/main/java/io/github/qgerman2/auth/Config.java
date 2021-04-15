package io.github.qgerman2.auth;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class Config {
    private static JavaPlugin Plugin;
    private static FileConfiguration config;
    public static void initialize(JavaPlugin Plugin) {
        Config.Plugin = Plugin;
        Plugin.saveDefaultConfig();
        config = Plugin.getConfig();
    }
    public static Map<String, Object> getDatabase() {
        return config.getConfigurationSection("db").getValues(false);
    }
}
