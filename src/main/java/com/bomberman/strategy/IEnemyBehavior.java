package com.bomberman.strategy;

import com.bomberman.models.Enemy;

public interface IEnemyBehavior {
    // Düşman bir sonraki hamlesini hesaplar
    void move(Enemy enemy);

    // Davranış tipini döndür
    String getBehaviorName();
}