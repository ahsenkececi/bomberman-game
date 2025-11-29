package com.bomberman.models;

public class HardWall extends Wall {
    private int health;

    public HardWall(int x, int y, String theme) {
        super(x, y, theme);
        this.health = 3; // 3 vuruş gerekir
    }

    @Override
    public boolean takeDamage() {
        health--;
        System.out.println("HardWall health: " + health);
        return health <= 0; // Health 0 olunca yok olur
    }

    @Override
    public String getSymbol() {
        return "H";
    }

    public int getHealth() {
        return health;
    }
}