package io.github.qgerman2.auth;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Config.initialize(this);
        Database.initialize(this);
        Spawn.initialize(this);
        EventListener.initialize(this);
        Auth.initialize(this);
        PlayerList.initialize();
        Commands.initialize(this);
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        getLogger().info("Auth plugin! mailtoken-mc");
    }
    @Override
    public void onDisable() {

    }

    public static void sendToBungeeCord(Player player, String pass){
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF(player.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendPluginMessage(Main.getPlugin(Main.class), "BungeeCord", b.toByteArray());
    }
}
