package com.brandtnewtonsoftware.asle.util;

import com.brandtnewtonsoftware.asle.User;
import com.brandtnewtonsoftware.asle.sign.Sign;

import java.sql.*;

/**
 * Created by Brandt on 10/29/2015.
 */
public class Database {

    public Database() {
        checkTables();
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            DriverManager.registerDriver(new org.sqlite.JDBC());
            connection = DriverManager.getConnection("jdbc:sqlite:data/asle.sqlite");
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private boolean tableExists(String tableName) {
        boolean exists = false;
        try {
            Connection connection = getConnection();
            Statement stmt = connection.createStatement();
            ResultSet results = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'");
            results.next();
            exists = !results.isClosed();
            results.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    private void checkTables() {
        if (!tableExists("AsleUsers")) {
            makeUsersTable();
        }
        if (!tableExists("Attempts")) {
            makeAttemptsTable();
        }
    }

    private void makeAttemptsTable() {
        try {
            Connection connection = getConnection();
            Statement stmt = connection.createStatement();
            stmt.execute("PRAGMA foreign_keys=ON;");
            boolean success = stmt.executeUpdate("CREATE TABLE Attempts (" +
                    "Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "User INTEGER, " +
                    "Sign TEXT, " +
                    "AttemptCount INTEGER, " +
                    "SuccessCount INTEGER, " +
                    "FOREIGN KEY(User) REFERENCES AsleUsers(Id)" +
                    ")") > 0;
            stmt.close();
            connection.commit();
            connection.close();
            if (success)
                System.out.println("Attempts Created.");
            else
                System.err.println("Could not create Attempts");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User loginUser(User user) {
        try {
            Connection connection = getConnection();
            PreparedStatement stmt;
            stmt = connection.prepareStatement("UPDATE AsleUsers SET LoginCount=? WHERE Id=?");
            stmt.setInt(1, user.getId());
            boolean success = stmt.execute();

            stmt.close();
            connection.commit();
            connection.close();

            if (success)
                user.incrementLoginCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static boolean isSafeChar(char character) {
        return ('0' <= character && character <= '9') ||
                ('A' <= character && character <= 'Z') ||
                ('a' <= character && character <= 'z') || character == ' ';
    }

    public static boolean isSafeString(CharSequence str) {
        for (int i = 0; i < str.length(); i++) {
            if (!isSafeChar(str.charAt(i)))
                return false;
        }
        return true;
    }

    public static String sanitizeString(CharSequence str) {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (isSafeChar(c))
                strBuilder.append(c);
        }
        return strBuilder.toString();
    }

    public User newUser(String name) {
        name = name.trim();
        if (!isSafeString(name) || name.isEmpty())
            return null;

        User user = null;

        try {
            Connection connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO AsleUsers (Name, LoginCount) VALUES (?,?)");
            stmt.setString(1, name);
            stmt.setInt(2, 0);
            boolean success = stmt.execute();
            if (success) {
                ResultSet results = connection.prepareStatement("SELECT last_insert_rowid()").executeQuery();
                if (results.next()) {
                    int id = (int) results.getLong(1);
                    user = new User(id, name, 0);
                }
            }
            stmt.close();
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User getUser(String name) {
        if (!isSafeString(name))
            return null;

        try {
            Connection connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT Id, LoginCount FROM AsleUsers WHERE Name LIKE ?");
            stmt.setString(1, name);
            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                int id = results.getInt("Id");
                int loginCount = results.getInt("LoginCount");
                results.close();
                return new User(id, name, loginCount);
            }
            stmt.close();
            connection.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Attempt getAttempt(User user, Sign sign) {
        try {
            Connection connection= getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT AttemptCount, SuccessCount FROM Attempts WHERE Sign LIKE ? AND User=?");
            stmt.setString(1, Integer.toString(sign.getValue()));
            stmt.setInt(2, user.getId());
            ResultSet results = stmt.executeQuery();
            results.next();
            int attemptCount = results.getInt("AttemptCount");
            int successCount = results.getInt("SuccessCount");
            return new Attempt(sign, attemptCount, successCount);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateAttempt(User user, Attempt attempt) {
        try {
            Connection connection = getConnection();
            boolean success;
            PreparedStatement stmt;
            if (getAttempt(user, attempt.getSign()) != null) {
                 stmt = connection.prepareStatement("UPDATE Attempts SET AttemptCount=?, SuccessCount=? WHERE User=? AND Sign LIKE ?");
                stmt.setInt(1, attempt.getAttemptCount());
                stmt.setInt(2, attempt.getSuccessCount());
                stmt.setInt(3, user.getId());
                stmt.setString(4, Integer.toString(attempt.getSign().getValue()));
                success = stmt.execute();
            } else {
                stmt = connection.prepareStatement("INSERT INTO Attempts (User, Sign, AttemptCount, SuccessCount) " +
                        "VALUES (?,?,?,?)");
                stmt.setInt(1, user.getId());
                stmt.setString(2, Integer.toString(attempt.getSign().getValue()));
                stmt.setInt(3, attempt.getAttemptCount());
                stmt.setInt(4, attempt.getSuccessCount());
                success = stmt.execute();
            }
            stmt.close();
            connection.commit();
            connection.close();
            if (success)
                System.out.println("Attempts Updated.");
            else
                System.err.println("Could not update Attempts");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void makeUsersTable() {
        try {
            Connection connection = getConnection();
            Statement stmt = connection.createStatement();
            boolean success = stmt.executeUpdate("CREATE TABLE AsleUsers (" +
                    "Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "Name TEXT, " +
                    "LoginCount INTEGER" +
                    ")") > 0;
            stmt.close();
            connection.commit();
            connection.close();
            if (success)
                System.out.println("AsleUsers Created.");
            else
                System.err.println("Could not create AsleUsers");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
