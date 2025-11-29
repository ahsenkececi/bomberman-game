package com.bomberman;

import com.bomberman.database.DatabaseManager;
import com.bomberman.factory.*;
import com.bomberman.models.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("🎮 BOMBERMAN GAME - DAY 3 TEST");
        System.out.println("================================\n");

        // Database bağlantısı
        DatabaseManager db = DatabaseManager.getInstance();
        System.out.println();

        // ============ WALL FACTORY TEST ============
        System.out.println("📋 TEST 1: Wall Factory Pattern");
        System.out.println("--------------------------------");

        Wall unbreakable = WallFactory.createWall(WallType.UNBREAKABLE, 0, 0, "Desert");
        Wall breakable = WallFactory.createWall(WallType.BREAKABLE, 1, 1, "Forest");
        Wall hard = WallFactory.createWall(WallType.HARD, 2, 2, "City");

        System.out.println("Created: " + unbreakable);
        System.out.println("Created: " + breakable);
        System.out.println("Created: " + hard);
        System.out.println();

        // Wall damage test
        System.out.println("Testing wall damage:");
        System.out.println("Unbreakable hit: destroyed = " + unbreakable.takeDamage());
        System.out.println("Breakable hit: destroyed = " + breakable.takeDamage());
        System.out.println("Hard wall hit 1: destroyed = " + hard.takeDamage());
        System.out.println("Hard wall hit 2: destroyed = " + hard.takeDamage());
        System.out.println("Hard wall hit 3: destroyed = " + hard.takeDamage());
        System.out.println();

        // ============ POWERUP FACTORY TEST ============
        System.out.println("📋 TEST 2: PowerUp Factory Pattern");
        System.out.println("-----------------------------------");

        PowerUp speedBoost = PowerUpFactory.createPowerUp(PowerUpType.SPEED_BOOST, 3, 3);
        PowerUp bombPower = PowerUpFactory.createPowerUp(PowerUpType.BOMB_POWER, 4, 4);
        PowerUp bombCount = PowerUpFactory.createPowerUp(PowerUpType.BOMB_COUNT, 5, 5);

        System.out.println("Created: " + speedBoost);
        System.out.println("Created: " + bombPower);
        System.out.println("Created: " + bombCount);
        System.out.println();

        // PowerUp effect test
        System.out.println("Testing power-up effects:");
        speedBoost.applyEffect();
        bombPower.applyEffect();
        bombCount.applyEffect();
        System.out.println();

        // Random PowerUp test
        System.out.println("Creating 3 random power-ups:");
        for (int i = 0; i < 3; i++) {
            PowerUp random = PowerUpFactory.createRandomPowerUp(i, i);
            System.out.println("  Random " + (i+1) + ": " + random.getName());
        }
        System.out.println();

        // ============ ENEMY FACTORY TEST ============
        System.out.println("📋 TEST 3: Enemy Factory Pattern");
        System.out.println("---------------------------------");

        Enemy staticEnemy = EnemyFactory.createEnemy(EnemyType.STATIC, 6, 6);
        Enemy chasingEnemy = EnemyFactory.createEnemy(EnemyType.CHASING, 7, 7);
        Enemy intelligentEnemy = EnemyFactory.createEnemy(EnemyType.INTELLIGENT, 8, 8);

        System.out.println("Created: " + staticEnemy);
        System.out.println("Created: " + chasingEnemy);
        System.out.println("Created: " + intelligentEnemy);
        System.out.println();

        // Enemy movement test
        System.out.println("Testing enemy movements:");
        staticEnemy.move(6, 6); // Static doesn't move
        chasingEnemy.move(7, 8); // Chasing moves
        intelligentEnemy.move(9, 9); // Intelligent moves
        System.out.println();

        // Enemy death test
        System.out.println("Testing enemy death:");
        chasingEnemy.die();
        System.out.println("Chasing enemy status: " + chasingEnemy);
        System.out.println();

        // Database bağlantısını kapat
        db.closeConnection();

        System.out.println("================================");
        System.out.println("✅ Day 3 Complete! 🎉\n");

        System.out.println("📊 PATTERN PROGRESS:");
        System.out.println("✅ Singleton Pattern (DatabaseManager)");
        System.out.println("✅ Repository Pattern (UserRepository)");
        System.out.println("✅ Factory Pattern (Wall, PowerUp, Enemy)");
        System.out.println("⬜ Strategy Pattern");
        System.out.println("⬜ Observer Pattern");
        System.out.println("⬜ Decorator Pattern");
        System.out.println("⬜ State Pattern");
        System.out.println("⬜ MVC Architecture");
    }
}

