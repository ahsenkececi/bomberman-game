package com.bomberman.decorator;

public class BasePlayer implements IPlayer {
    private String name;
    private int x;
    private int y;
    private int health;
    private float speed;
    private int bombCount;
    private int bombPower;

    public BasePlayer(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.health = 3;
        this.speed = 1.0f;
        this.bombCount = 1;
        this.bombPower = 1;
    }

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

    @Override
    public void move(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    @Override
    public void takeDamage() {
        health--;
    }

    @Override
    public String toString() {
        return name + " [HP:" + health + " SPD:" + speed + " BC:" + bombCount + " BP:" + bombPower + "]";
    }
}