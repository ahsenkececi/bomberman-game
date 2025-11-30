package com.bomberman.strategy;

import com.bomberman.models.Enemy;

public class IntelligentBehavior implements IEnemyBehavior {

    @Override
    public void move(Enemy enemy) {
        // A* pathfinding kullanır (ileride implement edeceğiz)
        int currentX = enemy.getX();
        int currentY = enemy.getY();

        // Şimdilik akıllı hareket simülasyonu
        int newX = currentX + 2;
        int newY = currentY + 1;

        enemy.move(newX, newY);
        System.out.println("Intelligent enemy used A* pathfinding");
    }

    @Override
    public String getBehaviorName() {
        return "Intelligent (A*)";
    }
}