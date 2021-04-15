package io.github.qgerman2.auth;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EventListener implements Listener {
    private static JavaPlugin Plugin;
    public static void initialize(JavaPlugin Plugin) {
        EventListener.Plugin = Plugin;
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(
                new PacketAdapter(Plugin, PacketType.Play.Client.getInstance().values()) {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        EventListener.onClientPacket(event);
                    }
                });
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerList.onPlayerJoin(event.getPlayer());
        Spawn.onPlayerJoin(event);
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PlayerList.onPlayerQuit(event.getPlayer());
    }
    private static void onClientPacket(PacketEvent event) {
        Spawn.onClientPacket(event);
    }
}
