package com.bomberman.decorator;

public abstract class PlayerDecorator implements IPlayer {
    protected IPlayer decoratedPlayer;

    public PlayerDecorator(IPlayer player) {
        this.decoratedPlayer = player;
    }

    @Override
    public String getName() {
        return decoratedPlayer.getName();
    }

    @Override
    public int getX() {
        return decoratedPlayer.getX();
    }

    @Override
    public int getY() {
        return decoratedPlayer.getY();
    }

    @Override
    public int getHealth() {
        return decoratedPlayer.getHealth();
    }

    @Override
    public float getSpeed() {
        return decoratedPlayer.getSpeed();
    }

    @Override
    public int getBombCount() {
        return decoratedPlayer.getBombCount();
    }

    @Override
    public int getBombPower() {
        return decoratedPlayer.getBombPower();
    }

    @Override
    public void move(int newX, int newY) {
        decoratedPlayer.move(newX, newY);
    }

    @Override
    public void takeDamage() {
        decoratedPlayer.takeDamage();
    }

    @Override
    public boolean isAlive(){
        return decoratedPlayer.isAlive();
    }

    @Override
    public String toString() {
        return decoratedPlayer.toString();
    }
}