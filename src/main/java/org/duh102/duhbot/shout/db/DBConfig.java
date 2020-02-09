package org.duh102.duhbot.shout.db;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.flywaydb.core.Flyway;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConfig {
    public ComboPooledDataSource dataSource = null;

    public DBConfig() {
        dataSource = new ComboPooledDataSource();
        dataSource.setJdbcUrl("jdbc:sqlite:shout.db");
        Flyway flyway = Flyway.configure().dataSource(dataSource).locations("classpath:db/migration","filesystem:db/migration").load();
        flyway.migrate();
    }
    public DataSource getDataSource() {
        return dataSource;
    }
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
