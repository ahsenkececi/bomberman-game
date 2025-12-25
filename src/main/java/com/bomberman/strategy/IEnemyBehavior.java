package com.bomberman.strategy;

import com.bomberman.models.Enemy;
import com.bomberman.models.Map;
import com.bomberman.models.Player;

public interface IEnemyBehavior {
    // Düşman bir sonraki hamlesini hesaplar
    void move(Enemy enemy, Map map, Player targetPlayer);

    // Davranış tipini döndür
    String getBehaviorName();
}