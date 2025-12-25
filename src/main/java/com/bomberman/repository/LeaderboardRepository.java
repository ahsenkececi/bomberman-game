package com.bomberman.repository;

import com.bomberman.database.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardRepository {

    private DatabaseManager dbManager;

    public LeaderboardRepository() {
        this.dbManager = DatabaseManager.getInstance();
    }

    // Yeni skor ekle
    public void addScore(int userId, int score) {
        String sql = "INSERT INTO leaderboard (user_id, score) VALUES (?, ?)";

        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, score);
            pstmt.executeUpdate();
            System.out.println("✅ Score added: " + score + " for user ID: " + userId);
        } catch (SQLException e) {
            System.err.println("❌ Failed to add score");
            e.printStackTrace();
        }
    }

    // Top skorları getir
    public List<LeaderboardEntry> getTopScores(int limit) {
        List<LeaderboardEntry> entries = new ArrayList<>();
        String sql = "SELECT u.username, l.score, l.game_date " +
                "FROM leaderboard l " +
                "JOIN users u ON l.user_id = u.id " +
                "ORDER BY l.score DESC LIMIT ?";

        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();

            int rank = 1;
            while (rs.next()) {
                entries.add(new LeaderboardEntry(
                        rank++,
                        rs.getString("username"),
                        rs.getInt("score"),
                        rs.getTimestamp("game_date")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Failed to get leaderboard");
            e.printStackTrace();
        }

        return entries;
    }

    // Kullanıcının en iyi skoru
    public int getUserBestScore(int userId) {
        String sql = "SELECT MAX(score) as best_score FROM leaderboard WHERE user_id = ?";

        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("best_score");
            }
        } catch (SQLException e) {
            System.err.println("❌ Failed to get best score for user ID: " + userId);
            e.printStackTrace();
        }

        return 0;
    }

    // Kullanıcının tüm skorları
    public List<Integer> getUserScores(int userId) {
        List<Integer> scores = new ArrayList<>();
        String sql = "SELECT score FROM leaderboard WHERE user_id = ? ORDER BY score DESC";

        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                scores.add(rs.getInt("score"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Failed to get user scores");
            e.printStackTrace();
        }

        return scores;
    }

    // İç sınıf - Leaderboard girdisi
    public static class LeaderboardEntry {
        private int rank;
        private String username;
        private int score;
        private Timestamp date;

        public LeaderboardEntry(int rank, String username, int score, Timestamp date) {
            this.rank = rank;
            this.username = username;
            this.score = score;
            this.date = date;
        }

        // Getters
        public int getRank() { return rank; }
        public String getUsername() { return username; }
        public int getScore() { return score; }
        public Timestamp getDate() { return date; }

        @Override
        public String toString() {
            return String.format("#%d - %s: %d points", rank, username, score);
        }
    }
}