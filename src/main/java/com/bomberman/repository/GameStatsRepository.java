package com.bomberman.repository;

import com.bomberman.database.DatabaseManager;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class GameStatsRepository {

    private DatabaseManager dbManager;

    public GameStatsRepository() {
        this.dbManager = DatabaseManager.getInstance();
    }

    // Kullanıcı için stats yoksa oluştur
    public void initializeStats(int userId) {
        String checkSql = "SELECT COUNT(*) FROM game_stats WHERE user_id = ?";
        String insertSql = "INSERT INTO game_stats (user_id, wins, losses, total_games) VALUES (?, 0, 0, 0)";

        try (PreparedStatement checkStmt = dbManager.getConnection().prepareStatement(checkSql)) {
            checkStmt.setInt(1, userId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) {
                // Stats yok, oluştur
                try (PreparedStatement insertStmt = dbManager.getConnection().prepareStatement(insertSql)) {
                    insertStmt.setInt(1, userId);
                    insertStmt.executeUpdate();
                    System.out.println("✅ Game stats initialized for user ID: " + userId);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Failed to initialize stats for user ID: " + userId);
            e.printStackTrace();
        }
    }

    // Oyun sonucu kaydet
    public void saveGameResult(int userId, boolean won) {
        // Önce stats var mı kontrol et
        initializeStats(userId);

        String sql = "UPDATE game_stats SET " +
                "wins = wins + ?, " +
                "losses = losses + ?, " +
                "total_games = total_games + 1 " +
                "WHERE user_id = ?";

        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, won ? 1 : 0);
            pstmt.setInt(2, won ? 0 : 1);
            pstmt.setInt(3, userId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Game result saved: " + (won ? "WIN" : "LOSS"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Failed to save game result");
            e.printStackTrace();
        }
    }

    // İstatistikleri getir
    public Map<String, Integer> getStats(int userId) {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT wins, losses, total_games FROM game_stats WHERE user_id = ?";

        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                stats.put("wins", rs.getInt("wins"));
                stats.put("losses", rs.getInt("losses"));
                stats.put("total_games", rs.getInt("total_games"));
            } else {
                // Stats yoksa 0 döndür
                stats.put("wins", 0);
                stats.put("losses", 0);
                stats.put("total_games", 0);
            }
        } catch (SQLException e) {
            System.err.println("❌ Failed to get stats for user ID: " + userId);
            e.printStackTrace();
        }

        return stats;
    }

    // Kazanma yüzdesi hesapla
    public double getWinRate(int userId) {
        Map<String, Integer> stats = getStats(userId);
        int totalGames = stats.get("total_games");

        if (totalGames == 0) {
            return 0.0;
        }

        return (stats.get("wins") * 100.0) / totalGames;
    }
}