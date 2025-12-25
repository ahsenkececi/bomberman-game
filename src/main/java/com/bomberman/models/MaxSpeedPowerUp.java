package com.bomberman.models;

public class MaxSpeedPowerUp extends PowerUp {

    public MaxSpeedPowerUp(int x, int y) {
        super(x, y);
    }

    @Override
    public void applyEffect() {
        System.out.println("⚡⚡ Max speed unlocked!");
    }

    @Override
    public String getName() {
        return "Max Speed";
    }

    @Override
    public String getSymbol() {
        return "⚡⚡";
    }
}