package com.bomberman.models;

import com.bomberman.repository.GameStatsRepository;
import com.bomberman.repository.LeaderboardRepository;

public class GameManager {

    // Singleton instance
    private static GameManager instance; //SINGLETON INSTANCE

    // Oyun verileri
    private Game currentGame;
    private User loggedInUser;
    private Theme selectedTheme;
    private boolean isMultiplayer;
    private int currentScore;

    // Private constructor SINGLETON
    private GameManager() {
        this.selectedTheme = Theme.FOREST; // Default tema
        this.isMultiplayer = false;
        this.currentScore = 0;
    }

    // Singleton getInstance
    public static synchronized GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    // Oyun başlatma
    public void startNewGame(User user, Theme theme, boolean multiplayer) {
        this.loggedInUser = user;
        this.selectedTheme = theme;
        this.isMultiplayer = multiplayer;
        this.currentScore = 0;
        this.currentGame = new Game(theme);

        System.out.println("🎮 New game started for user: " + user.getUsername());
    }

    // Oyunu sonlandırma
    public void endGame(boolean playerWon) {
        if (currentGame != null && loggedInUser != null) {
            System.out.println("🏁 Game ended. Winner: " + (playerWon ? "Player" : "Enemy"));

            // İstatistikleri kaydet
            GameStatsRepository statsRepo = new GameStatsRepository();
            statsRepo.saveGameResult(loggedInUser.getId(), playerWon);

            // Skoru kaydet
            LeaderboardRepository leaderboardRepo = new LeaderboardRepository();
            leaderboardRepo.addScore(loggedInUser.getId(), currentScore);

            System.out.println("💾 Game stats and score saved to database");
        }
    }

    // Skor artırma
    public void addScore(int points) {
        this.currentScore += points;
    }

    // Getters
    public Game getCurrentGame() {
        return currentGame;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public Theme getSelectedTheme() {
        return selectedTheme;
    }

    public boolean isMultiplayer() {
        return isMultiplayer;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    // Setters
    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public void setSelectedTheme(Theme theme) {
        this.selectedTheme = theme;
    }

    // Clone'u engelle (Singleton için)
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cannot clone singleton instance");
    }
}