package com.bomberman.models;

public class Explosion {
    private int x;
    private int y;
    private int power;
    private float lifetime; // Ekranda kalma süresi

    public Explosion(int x, int y, int power) {
        this.x = x;
        this.y = y;
        this.power = power;
        this.lifetime = 0.5f; // 0.5 saniye ekranda kalacak
    }

    public void update(float deltaTime) {
        lifetime -= deltaTime;
    }

    public boolean isFinished() {
        return lifetime <= 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPower() {
        return power;
    }
}