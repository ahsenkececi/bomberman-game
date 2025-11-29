package com.bomberman.models;

public abstract class Wall {
    protected int x;
    protected int y;
    protected String theme;

    public Wall(int x, int y, String theme) {
        this.x = x;
        this.y = y;
        this.theme = theme;
    }

    // Her duvar tipi kendi hasar alma mantığını uygular
    public abstract boolean takeDamage();

    // Duvarın görsel temsili
    public abstract String getSymbol();

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getTheme() {
        return theme;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " at (" + x + "," + y + ") [" + theme + "]";
    }
}