package com.bomberman.decorator;

public class SpeedBoostDecorator extends PlayerDecorator {
    private float speedMultiplier = 1.5f;

    public SpeedBoostDecorator(IPlayer player) {
        super(player);
        System.out.println("⚡ Speed Boost applied to " + player.getName());
    }

    @Override
    public float getSpeed() {
        return decoratedPlayer.getSpeed() * speedMultiplier;
    }

    @Override
    public String toString() {
        return decoratedPlayer.toString() + " +SpeedBoost";
    }
}