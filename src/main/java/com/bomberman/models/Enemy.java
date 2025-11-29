package com.bomberman.models;

public class Enemy {
    private int x;
    private int y;
    private String behaviorType;
    private boolean isAlive;

    public Enemy(int x, int y, String behaviorType) {
        this.x = x;
        this.y = y;
        this.behaviorType = behaviorType;
        this.isAlive = true;
    }

    public void move(int newX, int newY) {
        this.x = newX;
        this.y = newY;
        System.out.println(behaviorType + " enemy moved to (" + x + "," + y + ")");
    }

    public void die() {
        isAlive = false;
        System.out.println(behaviorType + " enemy died!");
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getBehaviorType() {
        return behaviorType;
    }

    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public String toString() {
        return behaviorType + " Enemy at (" + x + "," + y + ") [" + (isAlive ? "Alive" : "Dead") + "]";
    }
}