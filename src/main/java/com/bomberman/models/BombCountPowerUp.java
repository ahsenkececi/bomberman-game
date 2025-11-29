package com.bomberman.models;

public class BombCountPowerUp extends PowerUp {

    public BombCountPowerUp(int x, int y) {
        super(x, y);
    }

    @Override
    public void applyEffect() {
        System.out.println("💣 Bomb Count activated! Can place more bombs.");
    }

    @Override
    public String getName() {
        return "BombCount";
    }

    @Override
    public String getSymbol() {
        return "C";
    }
}