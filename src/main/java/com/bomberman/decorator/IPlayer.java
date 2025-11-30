package com.bomberman.decorator;

public interface IPlayer {
    String getName();
    int getX();
    int getY();
    int getHealth();
    float getSpeed();
    int getBombCount();
    int getBombPower();

    void move(int newX, int newY);
    void takeDamage();
}