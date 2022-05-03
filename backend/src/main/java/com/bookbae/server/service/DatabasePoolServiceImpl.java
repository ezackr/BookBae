package com.bookbae.server.service;

import com.bookbae.server.DatabasePoolService;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DatabasePoolServiceImpl implements DatabasePoolService {
    private BasicDataSource dataSource;

    public DatabasePoolServiceImpl() {
        this.dataSource = new BasicDataSource();
        this.dataSource.setUrl("jdbc:h2:mem:db1");
        this.dataSource.setDriverClassName("org.h2.Driver");
        this.dataSource.setPoolPreparedStatements(true);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }
}