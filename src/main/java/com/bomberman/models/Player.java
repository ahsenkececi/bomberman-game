package com.bomberman.models;

import com.bomberman.observer.IObserver;

public class Player implements IObserver {
    private String name;
    private int x;
    private int y;
    private int health;
    private boolean isAlive;

    public Player(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.health = 3;
        this.isAlive = true;
    }

    // Observer Pattern - Olay bildirimi
    @Override
    public void onNotify(String eventType, Object data) {
        if (eventType.equals("BOMB_EXPLODED")) {
            Bomb bomb = (Bomb) data;

            // Patlama menzilinde miyim?
            int distance = Math.abs(x - bomb.getX()) + Math.abs(y - bomb.getY());

            if (distance <= bomb.getPower()) {
                takeDamage();
                System.out.println("  ⚠️  " + name + " hit by explosion!");
            } else {
                System.out.println("  ✅ " + name + " safe from explosion");
            }
        } else if (eventType.equals("POWERUP_COLLECTED")) {
            System.out.println("  🎁 " + name + " collected a power-up!");
        }
    }

    private void takeDamage() {
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

    public void move(int newX, int newY) {
        this.x = newX;
        this.y = newY;
        System.out.println(name + " moved to (" + x + "," + y + ")");
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public String toString() {
        return name + " at (" + x + "," + y + ") [Health: " + health + "]";
    }
}