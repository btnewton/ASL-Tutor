package com.brandtnewtonsoftware.asle.util;

import com.brandtnewtonsoftware.asle.knn.Sign;

import java.sql.*;

/**
 * Created by Brandt on 10/29/2015.
 */
public class Database {

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                DriverManager.registerDriver(new org.sqlite.JDBC());
                connection = DriverManager.getConnection("jdbc:sqlite:dat/hands.sqlite");
                connection.setAutoCommit(false);
                System.out.println("Opened database successfully");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
