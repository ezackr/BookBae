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

/**
 * Provides an implementation of {@link com.bookbae.server.DatabasePoolService DatabasePoolService}.
 * This implementation is {@link jakarta.enterprise.context.ApplicationScoped ApplicationScoped},
 * so there is only one instance that is managed by the framework that will be shared by all
 * root resource classes and providers that require it.
 */
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
        return this.dataSource.getConnection();
    }

    @Override
    public boolean isMockDatabase() {
        return false;
    }

    /**
     * Gets the connection URL used by the Microsoft SQL Driver to connect to the database
     * by reading the values from system properties
     * @return a connection URL suitable for passing to our {@link import org.apache.commons.dbcp2.BasicDataSource DataSource}
     */
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