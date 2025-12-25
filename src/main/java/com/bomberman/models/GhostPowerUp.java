package com.bomberman.models;

public class GhostPowerUp extends PowerUp {

    public GhostPowerUp(int x, int y) {
        super(x, y);
    }

    @Override
    public void applyEffect() {  // ✅ Parametre YOK
        System.out.println("👻 Ghost mode activated!");
        // TODO: Ghost mekanizması
    }

    @Override
    public String getName() {
        return "Ghost";
    }

    @Override
    public String getSymbol() {
        return "👻";
    }
}