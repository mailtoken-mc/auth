package io.github.qgerman2.auth;

import org.apache.commons.codec.digest.DigestUtils;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.scheduler.BukkitRunnable;
import org.mariadb.jdbc.Driver;

import java.sql.*;

public class Database {
    private static JavaPlugin Plugin;
    private static Connection conn;
    public static void initialize(JavaPlugin Plugin) {
        Database.Plugin = Plugin;
        connect();
    }
    private static void connect() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Class.forName("org.mariadb.jdbc.Driver");
                    conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/" + Config.getDB().get("name"),
                            Config.getDB().get("user").toString(), Config.getDB().get("pass").toString());
                    Plugin.getLogger().info("Successfully connected to database server");
                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Plugin);
    }
    //New player, check status
    public static void checkNewPlayer(String playerName) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM `serevr`.`cuenta` WHERE `user` = ? LIMIT 1");
                    stmnt.setString(1, playerName);
                    ResultSet result = stmnt.executeQuery();
                    if (result.next()) {
                        Auth.promoteLogin(playerName);
                    } else {
                        Auth.promoteToken(playerName);
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Plugin);
    }
    //Player sent a token
    public static void requestToken(String playerName, String token) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM `serevr`.`emails` WHERE `registered` = 0 AND LEFT(`hash`, 5) = ? LIMIT 1");
                    stmnt.setString(1, token.toLowerCase());
                    ResultSet result = stmnt.executeQuery();
                    if (result.next()) {
                        //Token is available, promote
                        Auth.promoteRegister(playerName, token);
                    } else {
                        //Token is wrong
                        Auth.failedToken(playerName);
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Plugin);
    }
    //Player is trying to register
    public static void requestRegister(String playerName, String pass, String token) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    //Check token first
                    PreparedStatement stmnt1 = conn.prepareStatement("UPDATE `serevr`.`emails` SET `registered` = 1 WHERE LEFT(`hash`, 5) = ? AND `registered` = 0 LIMIT 1");
                    stmnt1.setString(1, token);
                    stmnt1.executeUpdate();
                    //Check if a token was consumed or it has been used already
                    if (stmnt1.getUpdateCount() < 1) {
                        Auth.demoteToken(playerName);
                    } else {
                        PreparedStatement stmnt2 = conn.prepareStatement("INSERT INTO `serevr`.`cuenta` (`user`,`pass`) VALUES (?,?)");
                        stmnt2.setString(1, playerName);
                        stmnt2.setString(2, DigestUtils.sha256Hex(pass));
                        stmnt2.execute();
                        //Assume it went right
                        Auth.promoteLogin(playerName);
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Plugin);
    }
    public static void requestLogin(String playerName, String pass) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM `serevr`.`cuenta` WHERE `user` = ? AND `pass` = ? LIMIT 1");
                    stmnt.setString(1, playerName);
                    stmnt.setString(2, DigestUtils.sha256Hex(pass));
                    ResultSet result = stmnt.executeQuery();
                    if (result.next()) {
                        Auth.successfulLogin(playerName, pass);
                    } else {
                        Auth.failedLogin(playerName);
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Plugin);
    }


}
