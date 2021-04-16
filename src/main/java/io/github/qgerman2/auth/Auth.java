package io.github.qgerman2.auth;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class Auth {
    private static JavaPlugin Plugin;
    private static final PlayerList newPlayers = new PlayerList();
    private static final PlayerList loginPlayers = new PlayerList();
    private static final PlayerList registerPlayers = new PlayerList();
    private static final PlayerList tokenPlayers = new PlayerList();
    private static final Map<String, String> playerPerToken = new HashMap<String, String>();
    public static void initialize(JavaPlugin Plugin) {
        Auth.Plugin = Plugin;
        //Send instructions to players periodically
        new BukkitRunnable() {
            @Override
            public void run() {
                sendPrompt();
            }
        }.runTaskTimer(Plugin, 0, 20 * 13); //run every 13 seconds
    }
    private static void sendPrompt() {
        syncPlayerLists();
        for (String playerName : PlayerList.online) {
            Player player = Plugin.getServer().getPlayer(playerName);
            assert player != null;
            if (tokenPlayers.set().contains(playerName)) {
                player.sendMessage("Ingresa una clave única con " + ChatColor.YELLOW + "/clave <clave>");
            }
            if (loginPlayers.set().contains(playerName)) {
                player.sendMessage("Ingresa la contraseña para el usuario " + ChatColor.BLUE + playerName + ChatColor.WHITE +" con " + ChatColor.YELLOW + " /ingresar <contraseña>");
            }
            if (registerPlayers.set().contains(playerName)) {
                player.sendMessage("Ingresa una nueva contraseña con " + ChatColor.YELLOW + " /registrar <contraseña> <confirmar>");
            }
        }
    }
    private static void syncPlayerLists() {
        loginPlayers.sync();
        registerPlayers.sync();
        tokenPlayers.sync();
    }
    //Check player status when joining
    public static void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        newPlayers.set().add(playerName);
        Database.checkNewPlayer(playerName);
    }
    //First step, new player
    public static void promoteToken(String playerName) {
        tokenPlayers.set().add(playerName);
        newPlayers.set().remove(playerName);
        sendPrompt();
    }
    //Player has sent a token for checking
    public static void onTokenCommand(Player player, String token) {
        if (tokenPlayers.set().contains(player.getName())) {
            Database.requestToken(player.getName(), token);
        } else {
            if (loginPlayers.set().contains(player.getName())) {
                player.sendMessage(ChatColor.RED + "No puedes usar ese comando, ya existe una cuenta con el nombre de " + ChatColor.BLUE + player.getName());
            } else if (registerPlayers.set().contains(player.getName())) {
                player.sendMessage(ChatColor.RED + "No puedes usar ese comando, debes crear una contraseña nueva");
            }
        }
    }
    //Wrong token
    public static void failedToken(String playerName) {
        Player player = Plugin.getServer().getPlayer(playerName);
        assert player != null;
        player.sendMessage(ChatColor.RED + "La clave única ingresada es incorrecta");
    }
    //Right token
    public static void promoteRegister(String playerName, String token) {
        playerPerToken.put(playerName, token);
        registerPlayers.set().add(playerName);
        tokenPlayers.set().remove(playerName);
        sendPrompt();
    }
    //Player requests to register
    public static void onRegisterCommand(Player player, String pass) {
        if (registerPlayers.set().contains(player.getName())) {
            Database.requestRegister(player.getName(), pass, playerPerToken.get(player.getName()));
        } else {
            if (tokenPlayers.set().contains(player.getName())) {
                player.sendMessage(ChatColor.RED + "No puedes usar ese comando, primero debes ingresar una clave única");
            }
        }
    }
    //In between sending the right token and registering, the token has been consumed by someone else
    public static void demoteToken(String playerName) {
        registerPlayers.set().remove(playerName);
        tokenPlayers.set().add(playerName);
        playerPerToken.remove(playerName);
    }
    //Either a successful register or player already had an account
    public static void promoteLogin(String playerName) {
        registerPlayers.set().remove(playerName);
        loginPlayers.set().add(playerName);
        playerPerToken.remove(playerName);
        newPlayers.set().remove(playerName);
        sendPrompt();
    }
    //Attempt to login
    public static void onLoginCommand(Player player, String pass) {
        if (loginPlayers.set().contains(player.getName())) {
            Database.requestLogin(player.getName(), pass);
        }
    }
    //Login password was wrong
    public static void failedLogin(String playerName) {
        Player player = Plugin.getServer().getPlayer(playerName);
        assert player != null;
        player.sendMessage(ChatColor.RED + "La contraseña es incorrecta");
    }
    //Login successful, ask for waterfall to teleport to main server
    public static void successfulLogin(String playerName) {
        Player player = Plugin.getServer().getPlayer(playerName);
        loginPlayers.set().remove(playerName);
        assert player != null;
        player.sendMessage("Tamo");
    }
    //Remove entries if player quits
    public static void onPlayerQuit(String playerName) {
        syncPlayerLists();
        playerPerToken.remove(playerName);
    }
}
