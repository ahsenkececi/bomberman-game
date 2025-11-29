package com.bomberman.models;

public abstract class PowerUp {
    protected int x;
    protected int y;

    public PowerUp(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Her power-up kendi etkisini uygular
    public abstract void applyEffect();

    public abstract String getName();

    public abstract String getSymbol();

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return getName() + " at (" + x + "," + y + ")";
    }
}