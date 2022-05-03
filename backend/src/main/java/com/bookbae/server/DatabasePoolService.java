package com.bookbae.server;

import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DatabasePoolService {
    private BasicDataSource dataSource;

    public DatabasePoolService() {
        this.dataSource = new BasicDataSource();
        this.dataSource.setUrl("jdbc:h2:mem:db1");
        this.dataSource.setDriverClassName("org.h2.Driver");
        this.dataSource.setPoolPreparedStatements(true);
    }

    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }
}