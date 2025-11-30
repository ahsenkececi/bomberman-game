package com.bomberman.strategy;

import com.bomberman.models.Enemy;

public class ChasingBehavior implements IEnemyBehavior {

    @Override
    public void move(Enemy enemy) {
        // Basit takip: Rastgele bir yöne hareket et
        // (Gerçekte player pozisyonuna göre hareket eder)
        int currentX = enemy.getX();
        int currentY = enemy.getY();

        // Basit hareket simülasyonu
        int newX = currentX + (Math.random() > 0.5 ? 1 : -1);
        int newY = currentY + (Math.random() > 0.5 ? 1 : -1);

        enemy.move(newX, newY);
        System.out.println("Chasing enemy moved towards target");
    }

    @Override
    public String getBehaviorName() {
        return "Chasing";
    }
}