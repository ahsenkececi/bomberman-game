package com.bomberman.models;

public class BreakableWall extends Wall {

    public BreakableWall(int x, int y, String theme) {
        super(x, y, theme);
    }

    @Override
    public boolean takeDamage() {
        // Tek vuruşta yok olur
        return true;
    }

    @Override
    public String getSymbol() {
        return "B";
    }
}