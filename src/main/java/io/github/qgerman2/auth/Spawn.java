package io.github.qgerman2.auth;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;

public class Spawn {
    private static JavaPlugin Plugin;
    private static ArmorStand stand;
    private final static PlayerList movingPlayers = new PlayerList();
    public static void initialize(JavaPlugin Plugin) {
        Spawn.Plugin = Plugin;
        //Delete all entities except for players
        for (Entity entity : Plugin.getServer().getWorlds().get(0).getEntities()) {
            if (entity.getType() != EntityType.PLAYER) {
                entity.remove();
            }
        }
        //Create an armor stand for players to spectate
        Location l = new Location(Bukkit.getWorlds().get(0), 0, 150, 0);
        stand = (ArmorStand) Bukkit.getWorlds().get(0).spawnEntity(l, EntityType.ARMOR_STAND);
        stand.setCanMove(false);
        stand.setVisible(false);
        //Freeze already connected players
        for (Player player : Plugin.getServer().getOnlinePlayers()) {
            freezePlayer(player);
        }
        //Check periodically if any player is not spectating the armor stand
        new BukkitRunnable() {
            @Override
            public void run() {
                movingPlayers.sync();
                Iterator<String> iter = movingPlayers.set().iterator();
                while (iter.hasNext()) {
                    Player player = Bukkit.getServer().getPlayer(iter.next());
                    assert player != null;
                    freezePlayer(player);
                    iter.remove();
                }
            }
        }.runTaskTimer(Plugin, 0, 20 * 10); //run every 10 seconds
    }
    public static void freezePlayer(Player player) {
        Plugin.getLogger().info("Freezing player " + player.getName());
        if (player.getGameMode() != GameMode.SPECTATOR) {
            player.setGameMode(GameMode.SPECTATOR);
        }
        player.setSpectatorTarget(stand);
    }
    public static void onPlayerJoin(PlayerJoinEvent event) {
        freezePlayer(event.getPlayer());
    }
    public static void onClientPacket(PacketEvent event) {
        //Stop player from dismounting the armor stand
        if (event.getPacket().getType() == PacketType.Play.Client.ENTITY_ACTION
        ||  event.getPacket().getType() == PacketType.Play.Client.USE_ENTITY) {
            event.setCancelled(true);
        }
        //Position packets mean the player is roaming free, freeze him again
        if (event.getPacket().getType() == PacketType.Play.Client.POSITION
        ||  event.getPacket().getType() == PacketType.Play.Client.LOOK) {
            String name = event.getPlayer().getName();
            movingPlayers.set().add(name);
        }
    }
}
