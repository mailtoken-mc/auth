package io.github.qgerman2.auth;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.Connection;
import org.bukkit.Bukkit;
import org.mariadb.r2dbc.MariadbConnectionConfiguration;
import org.mariadb.r2dbc.MariadbConnectionFactory;

import java.time.Duration;

public class Database {
    private static ConnectionPool pool;
    public static void test() {
        try {
            Connection conn = pool.create().block();
            Bukkit.getLogger().info("conectao a sql");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            MariadbConnectionFactory connFactory = new MariadbConnectionFactory(conf);
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
