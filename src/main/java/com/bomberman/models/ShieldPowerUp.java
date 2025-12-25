package com.bomberman.models;

public class ShieldPowerUp extends PowerUp {

    public ShieldPowerUp(int x, int y) {
        super(x, y);  // ✅ Doğru
    }

    @Override
    public void applyEffect() {  // ✅ Parametre YOK
        System.out.println("🛡️ Shield power-up collected!");
        // TODO: Shield mekanizması
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