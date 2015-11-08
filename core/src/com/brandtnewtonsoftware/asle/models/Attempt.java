package com.brandtnewtonsoftware.asle.models;

import com.brandtnewtonsoftware.asle.models.modes.GameMode;
import com.brandtnewtonsoftware.asle.sign.Sign;
import com.brandtnewtonsoftware.asle.util.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by Brandt on 11/4/2015.
 */
public class Attempt {

    private Sign sign;
    private Integer timeToComplete;
    private Date attemptedAt;

    public Attempt(Sign sign) {
        this.sign = sign;
        attemptedAt = new Date();
    }

    public Attempt(Sign sign, int timeToComplete, Date attemptedAt) {
        this.sign = sign;
        this.timeToComplete = timeToComplete;
        this.attemptedAt = attemptedAt;
    }

    public void setTimeToComplete(int timeToComplete) {
        this.timeToComplete = timeToComplete;
    }

    public boolean isCompleted() {
        return timeToComplete != null;
    }

    public Date getAttemptedAt() {
        return attemptedAt;
    }

    public int getTimeToComplete() {
        return timeToComplete;
    }

    public Sign getSign() {
        return sign;
    }

    public void save(User user, GameMode gameMode) throws SQLException {
        Connection connection = new Database().getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = connection.prepareStatement("INSERT INTO Attempts (User, Sign, GameMode, TimeToComplete, TestedAt) " +
                    "VALUES (?,?,?,?,?)");
            stmt.setInt(1, user.getId());
            stmt.setString(2, Integer.toString(sign.getValue()));
            stmt.setString(3, gameMode.getName());
            stmt.setInt(4, timeToComplete);
            stmt.setString(5, Database.dateToSQLiteString(attemptedAt));

            if (stmt.executeUpdate() == 0) {
                System.err.print("Could not add sign: " + sign.getValue() + ", user: " + user.getId() + ", mode: " + gameMode.getName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null)
                stmt.close();
            if (connection != null)
                connection.close();
        }
    }
}
