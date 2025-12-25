package com.bomberman.strategy;

import com.bomberman.models.Enemy;
import com.bomberman.models.Map;
import com.bomberman.models.Player;

public class ChasingBehavior implements IEnemyBehavior {

    @Override
    public void move(Enemy enemy, Map map, Player targetPlayer) {
        int enemyX = enemy.getX();
        int enemyY = enemy.getY();
        int playerX = targetPlayer.getX();
        int playerY = targetPlayer.getY();

        // Oyuncuya olan mesafeyi hesapla
        int deltaX = playerX - enemyX;
        int deltaY = playerY - enemyY;

        int newX = enemyX;
        int newY = enemyY;

        // Hangi eksende daha uzak? Ona göre hareket et
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            // X ekseninde hareket et
            if (deltaX > 0) {
                newX = enemyX + 1;  // Sağa git
            } else if (deltaX < 0) {
                newX = enemyX - 1;  // Sola git
            }
        } else {
            // Y ekseninde hareket et
            if (deltaY > 0) {
                newY = enemyY + 1;  // Aşağı git
            } else if (deltaY < 0) {
                newY = enemyY - 1;  // Yukarı git
            }
        }

        // Duvar kontrolü (map varsa)
        if (map != null && !isWall(map, newX, newY)) {
            enemy.move(newX, newY);
            System.out.println("👹 Chasing enemy moved from (" + enemyX + "," + enemyY +
                    ") to (" + newX + "," + newY + ")");
        } else {
            System.out.println("👹 Chasing enemy blocked by wall at (" + newX + "," + newY + ")");
        }
    }

    // Duvar kontrolü
    private boolean isWall(Map map, int x, int y) {
        if (x < 0 || y < 0 || x >= map.getWidth() || y >= map.getHeight()) {
            return true;  // Sınır dışı
        }
        int cell = map.getCell(x, y);
        return cell != 0;  // 0 = boş, diğerleri = duvar
    }

    @Override
    public String getBehaviorName() {
        return "Chasing";
    }
}