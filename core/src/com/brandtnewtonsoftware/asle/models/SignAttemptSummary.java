package com.brandtnewtonsoftware.asle.models;

import com.brandtnewtonsoftware.asle.sign.Sign;
import com.brandtnewtonsoftware.asle.util.Database;
import com.sun.org.apache.bcel.internal.generic.SASTORE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Brandt on 11/8/2015.
 */
public class SignAttemptSummary {

    private double averageTimeToComplete;
    private int totalAttempts;
    private int totalSuccess;

    public static SignAttemptSummary getAttempt(User user, Sign sign) throws SQLException {
        Connection connection= new Database().getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = connection.prepareStatement("SELECT AVG(TimeToComplete) AS AverageTimeToComplete, SUM(Success) AS TotalSuccess, Count(*) AS TotalAttempts FROM Attempts WHERE Sign LIKE ? AND User=?");
            stmt.setString(1, Integer.toString(sign.getValue()));
            stmt.setInt(2, user.getId());

            ResultSet results = stmt.executeQuery();

            double averageTimeToComplete = results.getDouble("AverageTimeToComplete");
            int totalSuccess = results.getInt("TotalSuccess");
            int totalAttempts = results.getInt("TotalAttempts");
            results.close();

            return new SignAttemptSummary(averageTimeToComplete, totalAttempts, totalSuccess);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null)
                stmt.close();
            if (connection != null)
                connection.close();
        }

        return new SignAttemptSummary(0, 0, 0);
    }

    public SignAttemptSummary(double averageTimeToComplete, int totalAttempts, int totalSuccess) {
        this.averageTimeToComplete = averageTimeToComplete;
        this.totalAttempts = totalAttempts;
        this.totalSuccess = totalSuccess;
    }

    public double getAverageTimeToComplete() {
        return averageTimeToComplete;
    }

    public int getTotalAttempts() {
        return totalAttempts;
    }

    public int getTotalSuccess() {
        return totalSuccess;
    }
}
