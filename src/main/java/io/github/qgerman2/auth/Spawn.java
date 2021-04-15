package io.github.qgerman2.auth;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
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
    public static void onClientPacket(PacketEvent event) {
        //Stop player from dismounting the armor stand
        if (event.getPacket().getType() == PacketType.Play.Client.ENTITY_ACTION
        ||  event.getPacket().getType() == PacketType.Play.Client.USE_ENTITY) {
            event.setCancelled(true);
        }
    }
}
