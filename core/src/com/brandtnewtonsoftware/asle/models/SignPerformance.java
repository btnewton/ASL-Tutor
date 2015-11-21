package com.brandtnewtonsoftware.asle.models;

import com.brandtnewtonsoftware.asle.models.sign.Sign;
import com.brandtnewtonsoftware.asle.stage.game.GameStage;
import com.brandtnewtonsoftware.asle.stage.game.NormalGame;
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

    private final char signValue;
    private final double successRate;
    private final int totalAttempts;
    private final double averageTimeToComplete;

    public static List<SignPerformance> getSignPerformance(GameStage gameStage) {

        Character[] signs = new Character[]{'9', '8', '7', '6', '5', '4', '3', '2', '1', '0'};
        List<SignPerformance> signPerformances = new ArrayList<>();
        Connection connection = Database.getConnection();

        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT RecentAttempts.Sign, CAST(SuccessCount AS FLOAT) / COUNT(*) AS SuccessRate, COUNT(*) AS TotalAttempts, AvgTimeToComplete " +
                            "FROM RecentAttempts JOIN " +
                            "(SELECT Sign, COUNT(*) AS SuccessCount, AVG(RecentAttempts.TimeToComplete) AS AvgTimeToComplete FROM RecentAttempts WHERE TimeToComplete NOT NULL AND GameMode=? GROUP BY Sign) AS RecentSuccessAttempts ON RecentSuccessAttempts.Sign=RecentAttempts.Sign " +
                            "WHERE GameMode=? " +
                            "GROUP BY RecentAttempts.Sign ORDER BY SuccessRate ASC LIMIT 100");

            stmt.setString(1, gameStage.getName());
            stmt.setString(2, gameStage.getName());

            ResultSet results = stmt.executeQuery();

            while(results.next()) {
                char sign = results.getString("RecentAttempts.Sign").charAt(0);
                double successRate = results.getDouble("SuccessRate");
                int totalAttempts = results.getInt("TotalAttempts");
                double averageTimeToComplete = results.getInt("AvgTimeToComplete");
                signPerformances.add(new SignPerformance(sign, successRate, totalAttempts, averageTimeToComplete));
                signs[Character.getNumericValue(sign)] = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Character sign : signs) {
            if (sign != null) {
                signPerformances.add(0, new SignPerformance(sign, 0, 0, 0));
            }
        }

        return signPerformances;
    }

    public SignPerformance(char signValue, double successRate, int totalAttempts, double averageTimeToComplete) {
        this.signValue = signValue;
        this.successRate = successRate;
        this.totalAttempts = totalAttempts;
        this.averageTimeToComplete = averageTimeToComplete;
    }

    public double getAverageTimeToComplete() {
        return averageTimeToComplete;
    }

    public int getProficiencyRating() {
        return (int) (successRate * (NormalGame.INITIAL_DELAY - averageTimeToComplete) / NormalGame.INITIAL_DELAY);
    }

    public int getTotalAttempts() {
        return totalAttempts;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public char getSignValue() {
        return signValue;
    }
}
