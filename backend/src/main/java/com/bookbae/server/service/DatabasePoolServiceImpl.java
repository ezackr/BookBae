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
        this.dataSource.setUrl(getConnectionUrl());
        this.dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        this.dataSource.setPoolPreparedStatements(true);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    private String getConnectionUrl() {

        // grab database credentials from dbconn.properties file
        Properties configProps = new Properties();
        try {
            configProps.load(new FileInputStream("dbconn.properties"));
        } catch(FileNotFoundException e) {
            System.out.println(e);
        } catch(IOException e) {
            System.out.println(e);
        }

        String serverURL = configProps.getProperty("bookbae.server_url");
        String dbName = configProps.getProperty("bookbae.database_name");
        String adminName = configProps.getProperty("bookbae.username");
        String password = configProps.getProperty("bookbae.password");
        String connectionUrl =
                String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;" +
                                "trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;" +
                                "loginTimeout=30;",
                        serverURL, dbName, adminName, password);
        return connectionUrl;
    }
}