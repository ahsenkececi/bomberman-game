package com.bomberman.decorator;

public class BombPowerDecorator extends PlayerDecorator {
    private int powerIncrease = 1;

    public BombPowerDecorator(IPlayer player) {
        super(player);
        System.out.println("💥 Bomb Power increased for " + player.getName());
    }

    @Override
    public int getBombPower() {
        return decoratedPlayer.getBombPower() + powerIncrease;
    }

    @Override
    public String toString() {
        return decoratedPlayer.toString() + " +BombPower";
    }
}