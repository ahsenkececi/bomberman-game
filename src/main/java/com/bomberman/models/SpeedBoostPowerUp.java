package com.bomberman.models;

public class SpeedBoostPowerUp extends PowerUp {

    public SpeedBoostPowerUp(int x, int y) {
        super(x, y);
    }

    @Override
    public void applyEffect() {
        System.out.println("⚡ Speed Boost activated! Player speed increased.");
    }

    @Override
    public String getName() {
        return "SpeedBoost";
    }

    @Override
    public String getSymbol() {
        return "S";
    }
}