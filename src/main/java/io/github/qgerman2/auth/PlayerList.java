package io.github.qgerman2.auth;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class PlayerList {
    public final static Set<String> online = new HashSet<String>();
    private final Set<String> players = new HashSet<String>();
    public Set<String> set() {
        return players;
    }
    public void sync() {
        players.removeIf(s -> !online.contains(s));
    }
    public static void onPlayerJoin(Player player) {
        online.add(player.getName());
    }
    public static void onPlayerQuit(Player player) {
        online.remove(player.getName());
    }
    public static void initialize() {
        //Fill list with already connected players in case of plugin reload
        for (Player player : Bukkit.getOnlinePlayers()) {
            online.add(player.getName());
        }
    }
}
