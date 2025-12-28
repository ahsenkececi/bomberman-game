package com.bomberman.models;

public class ShieldPowerUp extends PowerUp {

    public ShieldPowerUp(int x, int y) {
        super(x, y);
    }

    @Override
    public void applyEffect() {
        System.out.println("🛡️ Shield power-up collected!");
    }

    @Override
    public String getName() {
        return "Shield";
    }

    @Override
    public String getSymbol() {
        return "🛡️";
    }
}