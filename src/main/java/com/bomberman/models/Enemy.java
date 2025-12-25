package com.bomberman.models;

import com.bomberman.strategy.IEnemyBehavior;

public class Enemy {
    private int x;
    private int y;
    private IEnemyBehavior behavior;  // Strategy Pattern!
    private boolean isAlive;

    // ✅ YENİ: Hareket timer'ı
    private float moveTimer;
    private float moveDelay; // Saniye cinsinden hareket aralığı

    public Enemy(int x, int y, IEnemyBehavior behavior) {
        this.x = x;
        this.y = y;
        this.behavior = behavior;
        this.isAlive = true;

        // ✅ YENİ: Her enemy tipi için farklı hız
        this.moveTimer = 0;
        this.moveDelay = 0.5f; // Default: 0.5 saniyede bir hareket
    }

    // ✅ YENİ: Hareket hızını ayarla
    public void setMoveDelay(float delay) {
        this.moveDelay = delay;
    }

    // Strategy Pattern - Davranışı değiştir
    public void setBehavior(IEnemyBehavior behavior) {
        this.behavior = behavior;
        System.out.println("Enemy behavior changed to: " + behavior.getBehaviorName());
    }

    // ✅ GÜNCELLEME: Map ve Player parametresi ekle
    public void performMove(Map map, Player targetPlayer) {
        if (behavior != null && isAlive) {
            behavior.move(this, map, targetPlayer);
        }
    }

    // ✅ GÜNCELLEME: Timer kontrolü ile hareket
    public void update(float deltaTime, com.bomberman.models.Map map, Player targetPlayer) {
        if (!isAlive) return;

        moveTimer += deltaTime;

        // Hareket zamanı geldi mi?
        if (moveTimer >= moveDelay) {
            moveTimer = 0; // Reset timer

            if (behavior != null) {
                behavior.move(this, map, targetPlayer);
            }
        }
    }

    // ✅ Eski metod da korunsun (geriye dönük uyumluluk için)
    @Deprecated
    public void performMove() {
        if (behavior != null && isAlive) {
            System.out.println("⚠️ Warning: performMove() called without Map and Player");
            // Eski behavior metodunu çağır (uyumluluk için)
        }
    }

    public void move(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public void die() {
        isAlive = false;
        System.out.println("💀 " + behavior.getBehaviorName() + " enemy died at (" + x + "," + y + ")");
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public IEnemyBehavior getBehavior() {
        return behavior;
    }

    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public String toString() {
        return behavior.getBehaviorName() + " Enemy at (" + x + "," + y + ") [" + (isAlive ? "Alive" : "Dead") + "]";
    }
}