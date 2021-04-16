package io.github.qgerman2.auth;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Commands {
    private static JavaPlugin Plugin;
    public static void initialize(JavaPlugin Plugin) {
        Commands.Plugin = Plugin;
        commandLogin();
        commandRegister();
        commandToken();
    }
    public static void commandLogin() {
        Plugin.getCommand("ingresar").setExecutor((sender, cmd, label, args) -> {
            //Check if player
            if (!(sender instanceof Player))
                return true;
            //Check if theres 1 argument
            if (args.length != 1)
                return true;
            Player player = (Player) sender;
            Auth.onLoginCommand(player, args[0]);
            return true;
        });
    }
    public static void commandRegister() {
        Plugin.getCommand("registrar").setExecutor((sender, cmd, label, args) -> {
            //Check if player
            if (!(sender instanceof Player))
                return true;
            //Check if theres 2 arguments
            if (args.length != 2)
                return true;
            Player player = (Player) sender;
            //Check if args are equal
            if (!args[0].equals(args[1])) {
                player.sendMessage(ChatColor.RED + "Las contraseñas son distintas");
                return true;
            }
            Auth.onRegisterCommand(player, args[0]);
            return true;
        });
    }
    public static void commandToken() {
        Plugin.getCommand("clave").setExecutor((sender, cmd, label, args) -> {
            //Check if player
            if (!(sender instanceof Player))
                return true;
            //Check if theres 1 argument
            if (args.length != 1)
                return true;
            Player player = (Player) sender;
            Auth.onTokenCommand(player, args[0]);
            return true;
        });
    }
}
