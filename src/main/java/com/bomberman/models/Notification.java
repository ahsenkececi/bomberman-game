package com.bomberman.models;

public class Notification {
    private String message;
    private float lifetime;
    private int x;
    private int y;

    public Notification(String message, int x, int y) {
        this.message = message;
        this.x = x;
        this.y = y;
        this.lifetime = 2.0f; // 2 saniye ekranda kalacak
    }

    public void update(float deltaTime) {
        lifetime -= deltaTime;
        y -= 1; // Yukarı doğru kayacak
    }

    public boolean isFinished() {
        return lifetime <= 0;
    }

    public String getMessage() {
        return message;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public float getAlpha() {
        // Fade out efekti
        return Math.max(0, Math.min(1, lifetime / 2.0f));
    }
}