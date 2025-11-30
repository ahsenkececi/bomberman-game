package com.bomberman.models;

import com.bomberman.strategy.IEnemyBehavior;

public class Enemy {
    private int x;
    private int y;
    private IEnemyBehavior behavior;  // Strategy Pattern!
    private boolean isAlive;

    public Enemy(int x, int y, IEnemyBehavior behavior) {
        this.x = x;
        this.y = y;
        this.behavior = behavior;
        this.isAlive = true;
    }

    // Strategy Pattern - Davranışı değiştir
    public void setBehavior(IEnemyBehavior behavior) {
        this.behavior = behavior;
        System.out.println("Enemy behavior changed to: " + behavior.getBehaviorName());
    }

    // Strategy Pattern - Davranışa göre hareket et
    public void performMove() {
        if (behavior != null && isAlive) {
            behavior.move(this);
        }
    }

    public void move(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public void die() {
        isAlive = false;
        System.out.println(behavior.getBehaviorName() + " enemy died at (" + x + "," + y + ")");
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