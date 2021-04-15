package io.github.qgerman2.auth;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Spawn {
    private static JavaPlugin Plugin;
    private static ArmorStand stand;
    public static void initialize(JavaPlugin Plugin) {
        Spawn.Plugin = Plugin;
        Location l = new Location(Bukkit.getWorlds().get(0), 0, 150, 0);
        stand = (ArmorStand) Bukkit.getWorlds().get(0).spawnEntity(l, EntityType.ARMOR_STAND);
        stand.setCanMove(false);
    }
    public static void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().setSpectatorTarget(stand);
    }
}
