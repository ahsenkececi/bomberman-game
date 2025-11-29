package com.bomberman.models;

public class UnbreakableWall extends Wall {

    public UnbreakableWall(int x, int y, String theme) {
        super(x, y, theme);
    }

    @Override
    public boolean takeDamage() {
        // Yok edilemez
        return false;
    }

    @Override
    public String getSymbol() {
        return "#";
    }
}