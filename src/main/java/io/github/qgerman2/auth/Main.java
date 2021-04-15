package io.github.qgerman2.auth;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        Config.initialize(this);
        Database.initialize(this);
        getLogger().info("Auth plugin! mailtoken-mc");
    }
    @Override
    public void onDisable() {

    }
}
