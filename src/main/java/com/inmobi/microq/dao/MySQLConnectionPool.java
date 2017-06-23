package com.inmobi.microq.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * @author prathik.raj
 */
@Slf4j
public class MySQLConnectionPool {
    private static MySQLConnectionPool connectionPool = null;

    HikariDataSource ds = null;

    private MySQLConnectionPool() throws ConfigurationException {
        HikariConfig config = new HikariConfig();
        Configuration props = new PropertiesConfiguration("mysql.properties");
        config.setJdbcUrl(props.getString("url"));
        config.setUsername(props.getString("user"));
        config.setPassword(props.getString("password"));

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        ds = new HikariDataSource(config);
    }

    public HikariDataSource getDs() {
        return ds;
    }

    public static MySQLConnectionPool getInstance() throws ConfigurationException {
        if(connectionPool == null) {
            connectionPool = new MySQLConnectionPool();
        }

        return connectionPool;
    }
}
