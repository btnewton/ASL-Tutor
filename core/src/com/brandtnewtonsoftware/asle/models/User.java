package com.brandtnewtonsoftware.asle.models;

import com.brandtnewtonsoftware.asle.util.Database;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Brandt on 11/4/2015.
 */
public class User {

    private Integer id;
    private final String name;
    private int loginCount;

    public User(String name) {
        this.name = name;
        loginCount = 0;
    }

    private User(int id, String name, int loginCount) {
        this.id = id;
        this.name = name;
        this.loginCount = loginCount;
    }

    public static User getUser(String name) throws SQLException {
        Connection connection = Database.getConnection();
        User user = null;
        PreparedStatement stmt = null;

        try {
            stmt = connection.prepareStatement("SELECT Id, LoginCount FROM Users WHERE Name LIKE ?");
            stmt.setString(1, name);
            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                int id = results.getInt("Id");
                int loginCount = results.getInt("LoginCount");
                results.close();
                user = new User(id, name, loginCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null)
                stmt.close();
            if (connection != null)
                connection.close();
        }
        return user;
    }

    public void save() throws SQLException {
        if (id == null) {
            insert();
        } else {
            update();
        }
    }

    private void insert() throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = connection.prepareStatement("INSERT INTO Users (Name, LoginCount) VALUES (?,?)");
            stmt.setString(1, name);
            stmt.setInt(2, loginCount);
            boolean success = stmt.executeUpdate() > 0;
            if (success) {
                ResultSet results = connection.prepareStatement("SELECT last_insert_rowid()").executeQuery();
                if (results.next()) {
                    id = (int) results.getLong(1);
                }
                results.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally  {
            if (stmt != null)
                stmt.close();
            if (connection != null)
                connection.close();
        }
    }

    private void update() throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement stmt = null;

        try{
            stmt = connection.prepareStatement("UPDATE Users SET LoginCount=? WHERE Id=?");
            stmt.setInt(1, loginCount);
            stmt.setInt(2, getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }



    public int getId() {
        return id;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void incrementLoginCount() {
        loginCount++;
    }

    public String getName() {
        return name;
    }
}
