package com.bookbae.server;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import jakarta.inject.Singleton;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.security.SecureRandom;

@Singleton
@ApplicationPath("/v1")
public class RestApplication extends Application {
    private static byte[] keyBytes;

    private BasicDataSource dataSource;

    public RestApplication() {
        this.dataSource = new BasicDataSource();
        this.dataSource.setUrl("jdbc:h2:mem:db1");
        this.dataSource.setDriverClassName("org.h2.Driver");
        this.dataSource.setPoolPreparedStatements(true);
        keyBytes = new byte[64];
        SecureRandom rand = new SecureRandom();
        rand.nextBytes(keyBytes);
    }

    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    public SecretKey getKey() {
        return Keys.hmacShaKeyFor(keyBytes);
    }
}