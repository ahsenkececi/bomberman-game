package com.bomberman.strategy;

import com.bomberman.models.Enemy;
import com.bomberman.models.Map;
import com.bomberman.models.Player;

public class StaticBehavior implements IEnemyBehavior {

    @Override
    public void move(Enemy enemy, Map map, Player targetPlayer) {
        // Hareket etme
        System.out.println("Static enemy stays at (" + enemy.getX() + "," + enemy.getY() + ")");
    }

    @Override
    public String getBehaviorName() {
        return "Static";
    }
}