package io.github.qgerman2.auth;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;

import io.r2dbc.spi.Connection;
import org.bukkit.plugin.java.JavaPlugin;
import org.mariadb.r2dbc.MariadbConnectionConfiguration;
import org.mariadb.r2dbc.MariadbConnectionFactory;

import java.time.Duration;

public class Main extends JavaPlugin {
    private static MariadbConnectionFactory connFactory;
    private static ConnectionPool pool;
    @Override
    public void onEnable() {
        getLogger().info("test");
        initConnectionFactory();
        try {
            Connection conn = pool.create().block();
            getLogger().info("conectao a sql");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {

    }

    public static void initConnectionFactory() {
        try {
            MariadbConnectionConfiguration conf =
                    MariadbConnectionConfiguration.builder()
                    .host("localhost")
                    .port(3306)
                    .username("paper")
                    .password("miau")
                    .database("serevr")
                    .build();
            connFactory = new MariadbConnectionFactory(conf);
            ConnectionPoolConfiguration confPool = ConnectionPoolConfiguration
                    .builder(connFactory)
                    .maxIdleTime(Duration.ofMillis(1000))
                    .maxSize(20)
                    .build();
            pool = new ConnectionPool(confPool);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
