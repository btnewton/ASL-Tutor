package com.brandtnewtonsoftware.asle.models;

import com.brandtnewtonsoftware.asle.models.sign.Sign;
import com.brandtnewtonsoftware.asle.stage.game.GameStage;
import com.brandtnewtonsoftware.asle.util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

    public void save(User user, GameStage gameStage) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = connection.prepareStatement("INSERT INTO Attempts (User, Sign, GameMode, TimeToComplete, TestedAt) " +
                    "VALUES (?,?,?,?,?)");
            stmt.setInt(1, user.getId());
            stmt.setString(2, Character.toString(sign.getValue()));
            stmt.setString(3, gameStage.getName());
            if (timeToComplete != null)
                stmt.setInt(4, timeToComplete);
            else
                stmt.setNull(4, Types.INTEGER);
            stmt.setString(5, Database.dateToSQLiteString(attemptedAt));

            if (stmt.executeUpdate() == 0) {
                System.err.print("Could not add sign: " + sign.getValue() + ", user: " + user.getId() + ", mode: " + gameStage.getName());
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
