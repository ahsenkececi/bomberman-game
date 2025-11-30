package com.bomberman.strategy;

import com.bomberman.models.Enemy;

public class StaticBehavior implements IEnemyBehavior {

    @Override
    public void move(Enemy enemy) {
        // Hareket etme - durağan düşman
        System.out.println("Static enemy stays at (" + enemy.getX() + "," + enemy.getY() + ")");
    }

    @Override
    public String getBehaviorName() {
        return "Static";
    }
}