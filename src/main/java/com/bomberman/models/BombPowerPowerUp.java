package com.bomberman.models;

public class BombPowerPowerUp extends PowerUp {

    public BombPowerPowerUp(int x, int y) {
        super(x, y);
    }

    @Override
    public void applyEffect() {
        System.out.println("💥 Bomb Power activated! Explosion range increased.");
    }

    @Override
    public String getName() {
        return "BombPower";
    }

    @Override
    public String getSymbol() {
        return "P";
    }
}