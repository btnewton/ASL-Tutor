package com.brandtnewtonsoftware.asle.models;

import com.brandtnewtonsoftware.asle.models.sign.Sign;
import com.brandtnewtonsoftware.asle.stage.game.GameStage;
import com.brandtnewtonsoftware.asle.util.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brandt on 11/9/2015.
 */
public class SignPerformance {

    private String signValue;
    private double successRate;
    private int totalAttempts;

    public static List<SignPerformance> getSignPerformance(GameStage gameStage) {

        String[] signs = new String[]{"9", "8", "7", "6", "5", "4", "3", "2", "1", "0"};
        List<SignPerformance> signPerformances = new ArrayList<>();
        Connection connection = Database.getConnection();

        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT RecentAttempts.Sign, CAST(SuccessCount AS FLOAT) / COUNT(*) AS SuccessRate, COUNT(*) AS TotalAttempts " +
                            "FROM RecentAttempts LEFT JOIN " +
                            "(SELECT Sign, COUNT(*) AS SuccessCount FROM RecentAttempts WHERE TimeToComplete NOT NULL AND GameMode=? GROUP BY Sign) AS RecentSuccessAttempts ON RecentSuccessAttempts.Sign=RecentAttempts.Sign " +
                            "WHERE GameMode=? " +
                            "GROUP BY RecentAttempts.Sign ORDER BY SuccessRate ASC LIMIT 100");

            stmt.setString(1, gameStage.getName());
            stmt.setString(2, gameStage.getName());

            ResultSet results = stmt.executeQuery();

            while(results.next()) {
                String sign = results.getString(1);
                double successRate = results.getDouble(2);
                int totalAttempts = results.getInt(3);
                signPerformances.add(new SignPerformance(sign, successRate, totalAttempts));
                signs[Integer.parseInt(sign)] = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (String sign : signs) {
            if (sign != null) {
                signPerformances.add(0, new SignPerformance(sign, 0, 0));
            }
        }

        return signPerformances;
    }

    public SignPerformance(String signValue, double successRate, int totalAttempts) {
        this.signValue = signValue;
        this.successRate = successRate;
        this.totalAttempts = totalAttempts;
    }


    public int getTotalAttempts() {
        return totalAttempts;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public String getSignValue() {
        return signValue;
    }
}
