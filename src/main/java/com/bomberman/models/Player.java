package com.bomberman.models;

import com.bomberman.decorator.IPlayer;
import com.bomberman.observer.IObserver;

public class Player implements IObserver, IPlayer {
    private String name;
    private int x;
    private int y;
    private int health;
    private boolean isAlive;

    // ✅ Sadece BASE değerler
    private float speed;
    private int bombCount;
    private int bombPower;
    private int currentBombs; // Yerleştirilmiş bomba sayısı

    public Player(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.health = 3;
        this.isAlive = true;

        // Base değerler
        this.speed = 1.0f;
        this.bombPower = 1;
        this.bombCount = 1;
        this.currentBombs = 0;
    }

    // ❌ KALDIRILDI: Tüm temp değişkenler
    // ❌ KALDIRILDI: addSpeedBoost(), addBombPower(), addBombCount()
    // ❌ KALDIRILDI: updateTimers()

    // Hareket metodları aynen kalır
    public boolean move(int dx, int dy, Map map) {
        int newX = x + dx;
        int newY = y + dy;

        if (map.isWalkable(newX, newY)) {
            x = newX;
            y = newY;
            return true;
        }
        return false;
    }

    public boolean moveUp(Map map) {
        return move(0, -1, map);
    }

    public boolean moveDown(Map map) {
        return move(0, 1, map);
    }

    public boolean moveLeft(Map map) {
        return move(-1, 0, map);
    }

    public boolean moveRight(Map map) {
        return move(1, 0, map);
    }

    // Bomba yerleştir
    public boolean canPlaceBomb() {
        return currentBombs < bombCount;
    }

    public Bomb placeBomb() {
        if (canPlaceBomb()) {
            currentBombs++;
            Bomb bomb = new Bomb(x, y, bombPower);
            System.out.println("💣 " + name + " placed a bomb at (" + x + "," + y + ")");
            return bomb;
        }
        return null;
    }

    public void bombExploded() {
        if (currentBombs > 0) {
            currentBombs--;
        }
    }

    // Observer Pattern
    @Override
    public void onNotify(String eventType, Object data) {
        if (eventType.equals("BOMB_EXPLODED")) {
            Bomb bomb = (Bomb) data;
            int distance = Math.abs(x - bomb.getX()) + Math.abs(y - bomb.getY());

            if (distance <= bomb.getPower()) {
                takeDamage();
                System.out.println("  ⚠️  " + name + " hit by explosion!");
            }
        }
    }

    // Hasar al
    public void takeDamage() {
        health--;
        System.out.println("  💔 " + name + " health: " + health);

        if (health <= 0) {
            die();
        }
    }

    private void die() {
        isAlive = false;
        System.out.println("  ☠️  " + name + " died!");
    }

    // ✅ IPlayer interface metodları
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public int getBombCount() {
        return bombCount;
    }

    @Override
    public int getBombPower() {
        return bombPower;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getCurrentBombs() {
        return currentBombs;
    }

    // ✅ IPlayer'dan gelen move
    @Override
    public void move(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    @Override
    public String toString() {
        return name + " at (" + x + "," + y + ") [HP:" + health + " Speed:" + speed +
                " Bombs:" + currentBombs + "/" + bombCount + " Power:" + bombPower + "]";
    }
    // ✅ GameWindow için placeholder metodlar (her zaman false)
    public boolean isShieldActive() {
        return false;  // Artık shield yok
    }

    public boolean isGhostMode() {
        return false;  // Artık ghost yok
    }

    public boolean isMaxSpeedActive() {
        return false;  // Artık max speed yok
    }

    public float getShieldTimer() {
        return 0f;
    }

    public float getGhostTimer() {
        return 0f;
    }

    public float getMaxSpeedTimer() {
        return 0f;
    }
    public float getTempSpeedTimer() {
        return 0f;
    }

    public float getTempBombPowerTimer() {
        return 0f;
    }

    public float getTempBombCountTimer() {
        return 0f;
    }

    public boolean hasActivePowerUp() {
        return false;
    }
}