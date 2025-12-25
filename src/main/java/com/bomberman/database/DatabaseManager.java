package com.bomberman.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    // Singleton instance
    private static DatabaseManager instance;

    // Database bağlantısı
    private Connection connection;

    // MySQL bağlantı bilgileri
    private static final String URL = "jdbc:mysql://localhost:3306/bomberman_db";
    private static final String USER = "root";  // Senin MySQL kullanıcı adın
    private static final String PASSWORD = "10102004Er";  // Senin MySQL şifren

    // Private constructor (dışarıdan new yapılamasın)
    private DatabaseManager() {
        try {
            // MySQL driver'ı yükle
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Bağlantı oluştur
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("✅ Database connection successful!");

            // Tabloları oluştur
            createTables();

        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed!");
            e.printStackTrace();
        }
    }

    // Singleton getInstance metodu
    public static DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        return instance;
    }

    // Connection'ı dışarıya ver
    public Connection getConnection() {
        return connection;
    }

    // Tabloları oluştur
    private void createTables() {
        try {
            Statement stmt = connection.createStatement();

            // Users tablosu
            String usersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    password_hash VARCHAR(255) NOT NULL,
                    preferred_theme VARCHAR(20) DEFAULT 'FOREST',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """;
            stmt.executeUpdate(usersTable);
            System.out.println("✅ Users table ready");

            // GameStats tablosu
            String statsTable = """
                CREATE TABLE IF NOT EXISTS game_stats (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    user_id INT NOT NULL,
                    wins INT DEFAULT 0,
                    losses INT DEFAULT 0,
                    total_games INT DEFAULT 0,
                    FOREIGN KEY (user_id) REFERENCES users(id)
                )
            """;
            stmt.executeUpdate(statsTable);
            System.out.println("✅ GameStats table ready");

            // Leaderboard tablosu
            String leaderboardTable = """
                CREATE TABLE IF NOT EXISTS leaderboard (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    user_id INT NOT NULL,
                    score INT NOT NULL,
                    game_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (user_id) REFERENCES users(id)
                )
            """;
            stmt.executeUpdate(leaderboardTable);
            System.out.println("✅ Leaderboard table ready");

            stmt.close();

        } catch (SQLException e) {
            System.err.println("❌ Table creation failed!");
            e.printStackTrace();
        }
    }

    // Bağlantıyı kapat
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ Database connection closed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}