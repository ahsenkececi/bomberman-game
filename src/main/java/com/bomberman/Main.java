package com.bomberman;

import com.bomberman.database.DatabaseManager;
import com.bomberman.factory.*;
import com.bomberman.models.*;
import com.bomberman.observer.GameEventManager;
import com.bomberman.strategy.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("🎮 BOMBERMAN GAME - DAY 4 TEST");
        System.out.println("================================\n");

        // Database
        DatabaseManager db = DatabaseManager.getInstance();
        System.out.println();

        // ============ STRATEGY PATTERN TEST ============
        System.out.println("📋 TEST 1: Strategy Pattern (Enemy AI)");
        System.out.println("---------------------------------------");

        // Factory ile düşman oluştur (Strategy otomatik atanır)
        Enemy staticEnemy = EnemyFactory.createEnemy(EnemyType.STATIC, 5, 5);
        Enemy chasingEnemy = EnemyFactory.createEnemy(EnemyType.CHASING, 10, 10);
        Enemy intelligentEnemy = EnemyFactory.createEnemy(EnemyType.INTELLIGENT, 15, 15);

        System.out.println("Created: " + staticEnemy);
        System.out.println("Created: " + chasingEnemy);
        System.out.println("Created: " + intelligentEnemy);
        System.out.println();

        // Stratejilerine göre hareket et
        System.out.println("Testing enemy movements:");
        staticEnemy.performMove();
        chasingEnemy.performMove();
        intelligentEnemy.performMove();
        System.out.println();

        // Runtime'da strateji değiştir!
        System.out.println("Changing strategy at runtime:");
        staticEnemy.setBehavior(new ChasingBehavior());
        staticEnemy.performMove();
        System.out.println();

        // ============ OBSERVER PATTERN TEST ============
        System.out.println("📋 TEST 2: Observer Pattern (Game Events)");
        System.out.println("------------------------------------------");

        GameEventManager eventManager = GameEventManager.getInstance();

        // Player'ları oluştur (Observer)
        Player player1 = new Player("Player1", 3, 3);
        Player player2 = new Player("Player2", 8, 8);

        System.out.println("Created: " + player1);
        System.out.println("Created: " + player2);
        System.out.println();

        // Observer'ları kaydet
        System.out.println("Registering observers:");
        eventManager.attach(player1);
        eventManager.attach(player2);
        System.out.println();

        // Bomba oluştur
        System.out.println("Creating bomb...");
        Bomb bomb1 = new Bomb(5, 5, 3);
        System.out.println(bomb1);
        System.out.println();

        // Bomba patla - tüm observer'lar bildirim alacak!
        System.out.println("💣 Bomb exploding...");
        bomb1.explode();
        System.out.println();

        // İkinci bomba - farklı pozisyon
        System.out.println("Creating second bomb...");
        Bomb bomb2 = new Bomb(8, 8, 2);
        System.out.println(bomb2);
        System.out.println();

        System.out.println("💣 Second bomb exploding...");
        bomb2.explode();
        System.out.println();

        // Observer çıkar
        System.out.println("Removing Player1 from observers:");
        eventManager.detach(player1);
        System.out.println();

        // Üçüncü bomba - sadece Player2 bildirim alacak
        System.out.println("Creating third bomb...");
        Bomb bomb3 = new Bomb(10, 10, 2);
        System.out.println("💣 Third bomb exploding...");
        bomb3.explode();
        System.out.println();

        // Database kapat
        db.closeConnection();

        System.out.println("================================");
        System.out.println("✅ Day 4 Complete! 🎉\n");

        System.out.println("📊 PATTERN PROGRESS:");
        System.out.println("✅ Singleton Pattern (DatabaseManager)");
        System.out.println("✅ Repository Pattern (UserRepository)");
        System.out.println("✅ Factory Pattern (Wall, PowerUp, Enemy)");
        System.out.println("✅ Strategy Pattern (Enemy AI Behaviors)");
        System.out.println("✅ Observer Pattern (Game Events)");
        System.out.println("⬜ Decorator Pattern");
        System.out.println("⬜ State Pattern");
        System.out.println("⬜ MVC Architecture");
        System.out.println("\n🎯 5/8 Patterns Complete! (62.5%)");
    }
}