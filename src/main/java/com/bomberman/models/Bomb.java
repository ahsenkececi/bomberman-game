package com.bomberman.models;

import com.bomberman.observer.GameEventManager;

public class Bomb {
    private int x;
    private int y;
    private int power;
    private float timer;
    private boolean exploded;

    public Bomb(int x, int y, int power) {
        this.x = x;
        this.y = y;
        this.power = power;
        this.timer = 3.0f; // 3 saniye
        this.exploded = false;
    }

    // Zamanlayıcıyı güncelle
    public void update(float deltaTime) {
        if (!exploded) {
            timer -= deltaTime;

            if (timer <= 0) {
                explode();
            }
        }
    }

    // Patla ve observer'lara bildir!
    public void explode() {
        if (!exploded) {
            exploded = true;
            System.out.println("💥 BOOM! Bomb exploded at (" + x + "," + y + ")");

            // Observer Pattern - Tüm observer'lara bildir
            GameEventManager.getInstance().notifyObservers("BOMB_EXPLODED", this);
        }
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPower() {
        return power;
    }

    public boolean isExploded() {
        return exploded;
    }

    @Override
    public String toString() {
        return "Bomb at (" + x + "," + y + ") [Power: " + power + ", Timer: " + timer + "s]";
    }
}