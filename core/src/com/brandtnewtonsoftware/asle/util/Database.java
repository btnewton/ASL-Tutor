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
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private boolean tableExists(String tableName) throws SQLException {
        boolean exists = false;
        Connection connection = getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement("SELECT name FROM sqlite_master WHERE type='table' AND name=?");
            stmt.setString(1, tableName);
            ResultSet results = stmt.executeQuery();
            results.next();
            exists = !results.isClosed();
            results.close();
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
        return exists;
    }

    private void checkTables() {
        try {
            if (!tableExists("AsleUsers")) {
                makeUsersTable();
            }
            if (!tableExists("Attempts")) {
                makeAttemptsTable();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(-1);
        }
    }

    private void makeAttemptsTable() throws SQLException {
        Connection connection = getConnection();
        Statement stmt = null;
        try {

            stmt = connection.createStatement();
            stmt.execute("PRAGMA foreign_keys=ON;");
            boolean success = stmt.executeUpdate("CREATE TABLE Attempts (" +
                    "Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "User INTEGER, " +
                    "Sign TEXT, " +
                    "AttemptCount INTEGER, " +
                    "SuccessCount INTEGER, " +
                    "FOREIGN KEY(User) REFERENCES AsleUsers(Id)" +
                    ")") > 0;
            if (success)
                System.out.println("Attempts Created.");
            else
                System.err.println("Could not create Attempts");
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

    public User loginUser(User user) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement stmt = null;

        try{
            user.incrementLoginCount();
            stmt = connection.prepareStatement("UPDATE AsleUsers SET LoginCount=? WHERE Id=?");
            stmt.setInt(1, user.getLoginCount());
            stmt.setInt(2, user.getId());
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

    public User newUser(String name) throws SQLException {
        name = name.trim();
        if (!isSafeString(name) || name.isEmpty())
            return null;

        Connection connection = getConnection();
        PreparedStatement stmt = null;
        User user = null;

        try {
            stmt = connection.prepareStatement("INSERT INTO AsleUsers (Name, LoginCount) VALUES (?,?)");
            stmt.setString(1, name);
            stmt.setInt(2, 0);
            boolean success = stmt.executeUpdate() > 0;
            if (success) {
                ResultSet results = connection.prepareStatement("SELECT last_insert_rowid()").executeQuery();
                if (results.next()) {
                    int id = (int) results.getLong(1);
                    user = new User(id, name, 0);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally  {
            if (stmt != null)
                stmt.close();
            if (connection != null)
                connection.close();
        }

        return user;
    }

    public User getUser(String name) throws SQLException {
        if (!isSafeString(name))
            return null;

        Connection connection = getConnection();
        User user = null;
        PreparedStatement stmt = null;

        try {
            stmt = connection.prepareStatement("SELECT Id, LoginCount FROM AsleUsers WHERE Name LIKE ?");
            stmt.setString(1, name);
            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                int id = results.getInt("Id");
                int loginCount = results.getInt("LoginCount");
                results.close();
                user = new User(id, name, loginCount);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null)
                stmt.close();
            if (connection != null)
                connection.close();
        }
        return user;
    }

    public Attempt getAttempt(User user, Sign sign) throws SQLException {
        Connection connection= getConnection();
        PreparedStatement stmt = null;
        Attempt attempt = null;
        try {
            stmt = connection.prepareStatement("SELECT AttemptCount, SuccessCount FROM Attempts WHERE Sign LIKE ? AND User=?");
            stmt.setString(1, Integer.toString(sign.getValue()));
            stmt.setInt(2, user.getId());
            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                int attemptCount = results.getInt("AttemptCount");
                int successCount = results.getInt("SuccessCount");
                attempt = new Attempt(sign, attemptCount, successCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null)
                stmt.close();
            if (connection != null)
                connection.close();
        }
        return attempt;
    }

    public void updateAttempt(User user, Attempt attempt) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement stmt = null;

        try {
            boolean success;
            if (getAttempt(user, attempt.getSign()) != null) {
                stmt = connection.prepareStatement("UPDATE Attempts SET AttemptCount=?, SuccessCount=? WHERE User=? AND Sign LIKE ?");
                stmt.setInt(1, attempt.getAttemptCount());
                stmt.setInt(2, attempt.getSuccessCount());
                stmt.setInt(3, user.getId());
                stmt.setString(4, Integer.toString(attempt.getSign().getValue()));
            } else {
                stmt = connection.prepareStatement("INSERT INTO Attempts (User, Sign, AttemptCount, SuccessCount) " +
                        "VALUES (?,?,?,?)");
                stmt.setInt(1, user.getId());
                stmt.setString(2, Integer.toString(attempt.getSign().getValue()));
                stmt.setInt(3, attempt.getAttemptCount());
                stmt.setInt(4, attempt.getSuccessCount());
            }
            success = stmt.executeUpdate() > 0;
            if (success)
                System.out.println("Attempts Updated.");
            else
                System.err.println("Could not update Attempts");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null)
                stmt.close();
            if (connection != null)
                connection.close();
        }
    }

    private void makeUsersTable() {
        Connection connection = getConnection();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
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
