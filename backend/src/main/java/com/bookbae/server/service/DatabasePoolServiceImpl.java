package com.bookbae.server.service;

import com.bookbae.server.DatabasePoolService;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@ApplicationScoped
public class DatabasePoolServiceImpl implements DatabasePoolService {
    private BasicDataSource dataSource;

    public DatabasePoolServiceImpl() {
        this.dataSource = new BasicDataSource();
        this.dataSource.setUrl(this.getConnectionUrl());
        this.dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        this.dataSource.setPoolPreparedStatements(true);
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection conn = this.dataSource.getConnection();
        conn.setAutoCommit(false);
        return conn;
    }

    private static String getConnectionUrl() {
        String serverURL = System.getProperty("bookbae.server_url");
        String dbName = System.getProperty("bookbae.database_name");
        String adminName = System.getProperty("bookbae.username");
        String password = System.getProperty("bookbae.password");
        return String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;" +
                                "trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;" +
                                "loginTimeout=30;",
                        serverURL, dbName, adminName, password);
    }
}