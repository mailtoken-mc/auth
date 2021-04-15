package io.github.qgerman2.auth;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Statement;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.mariadb.r2dbc.MariadbConnectionConfiguration;
import org.mariadb.r2dbc.MariadbConnectionFactory;
import reactor.core.publisher.Mono;

import java.util.Map;

public class Database {
    private static JavaPlugin Plugin;
    private static MariadbConnectionFactory connFactory;
    public static void initialize(JavaPlugin Plugin) {
        Database.Plugin = Plugin;
        connect();
    }
    private static void connect() {
        try {
            Map<String, Object> dbConf = Config.getDatabase();
            MariadbConnectionConfiguration conf =
                    MariadbConnectionConfiguration.builder()
                            .host((String) dbConf.get("host"))
                            .port((Integer) dbConf.get("port"))
                            .username((String) dbConf.get("user"))
                            .password((String) dbConf.get("pass"))
                            .database((String) dbConf.get("name"))
                            .build();
            connFactory = new MariadbConnectionFactory(conf);
            Bukkit.getLogger().info("conectao a sql");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
