package com.test.utils;

import com.test.config.DatabaseConfig;
import com.test.exception.ServiceException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utils for database
 */
public class JDBCUtils {

    public static Connection getConnection() {
        Connection connection;
        try {
            connection = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USERNAME, DatabaseConfig.PASSWORD);
            connection.setSchema(DatabaseConfig.DEFAULT_SCHEMA);
        } catch (SQLException e) {
            throw new ServiceException("Could not get a database connection", e);
        }
        return connection;
    }

}