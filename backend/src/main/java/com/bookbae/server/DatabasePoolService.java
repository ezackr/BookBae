package com.bookbae.server;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This interface defines the contract for accessing the database pool. The application
 * has one pool of connections created by this interface's implementing class, 
 * and root resource classes and providers may get a connection from that pool 
 * using their injected instance of this interface.
 * @see jakarta.inject.Inject
 */ 
public interface DatabasePoolService {
    /**
     * Gets a connection from the pool maintained by this service
     * @return a {@link java.sql.Connection Connection} to the server's database
     */
    public Connection getConnection() throws SQLException;

    /**
     * Required to work around the differences in syntax between our testing and production database
     * @return true if the database is H2, false if it is MSSQL
     */
    public boolean isMockDatabase();
}