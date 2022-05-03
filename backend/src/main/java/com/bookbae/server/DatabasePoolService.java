package com.bookbae.server;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabasePoolService {
    public Connection getConnection() throws SQLException;
}