package io.github.qgerman2.auth;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        Config.initialize(this);
        Database.initialize(this);
        Spawn.initialize(this);
        EventListener.initialize(this);
        PlayerList.initialize();
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        getLogger().info("Auth plugin! mailtoken-mc");
    }
    @Override
    public void onDisable() {

    }
}
