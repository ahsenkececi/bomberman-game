package com.bomberman.models;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Theme theme;  // ✅ Zaten var
    private List<Player> players;
    private List<Enemy> enemies;
    private List<Bomb> bombs;
    private List<Wall> walls;
    private List<PowerUp> powerUps;
    private boolean isRunning;
    private int turnCount;
    private int score;  // ✅ Skor ekleyin

    // ✅ YENİ: Theme parametreli constructor
    public Game(Theme theme) {
        this.theme = theme;
        this.players = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.bombs = new ArrayList<>();
        this.walls = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        this.isRunning = false;
        this.turnCount = 0;
        this.score = 0;

        System.out.println("🎮 Game created with theme: " + theme);
    }

    // ✅ ESKİ: Parametresiz constructor (eski kodlar için)
    public Game() {
        this(Theme.FOREST);  // Default tema
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    public void addBomb(Bomb bomb) {
        bombs.add(bomb);
    }

    public void addWall(Wall wall) {
        walls.add(wall);
    }

    public void addPowerUp(PowerUp powerUp) {
        powerUps.add(powerUp);
    }

    public void start() {
        isRunning = true;
        System.out.println("🎮 Game started!");
    }

    public void stop() {
        isRunning = false;
        System.out.println("⏸️  Game stopped!");
    }

    public void incrementTurn() {
        turnCount++;
    }

    // ✅ YENİ: Skor metodları
    public void addScore(int points) {
        this.score += points;
    }

    // Getters
    public Theme getTheme() {
        return theme;
    }

    public int getScore() {
        return score;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getTurnCount() {
        return turnCount;
    }
}