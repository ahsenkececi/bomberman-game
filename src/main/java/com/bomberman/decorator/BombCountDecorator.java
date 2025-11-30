package com.bomberman.decorator;

public class BombCountDecorator extends PlayerDecorator {
    private int countIncrease = 1;

    public BombCountDecorator(IPlayer player) {
        super(player);
        System.out.println("💣 Bomb Count increased for " + player.getName());
    }

    @Override
    public int getBombCount() {
        return decoratedPlayer.getBombCount() + countIncrease;
    }

    @Override
    public String toString() {
        return decoratedPlayer.toString() + " +BombCount";
    }
}