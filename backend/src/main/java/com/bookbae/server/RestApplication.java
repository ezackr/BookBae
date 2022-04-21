package com.bookbae.server;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import jakarta.inject.Singleton;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Singleton
@ApplicationPath("/v1")
public class RestApplication extends Application {

    private BasicDataSource dataSource;

    public RestApplication() {
        this.dataSource = new BasicDataSource();
        this.dataSource.setUrl("jdbc:h2:mem:db1");
        this.dataSource.setDriverClassName("org.h2.Driver");
        this.dataSource.setPoolPreparedStatements(true);
        // could initialize the testing db here
        // but in a real-world situation would be loading a pre-configured database
        //TODO: find out how to get runtime configuration info to do tests
    }

    public String getString() {
        return "never gonna give u up";
    }

    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }
}