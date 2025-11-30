package com.bomberman;

import com.bomberman.database.DatabaseManager;
import com.bomberman.decorator.*;
import com.bomberman.state.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("🎮 BOMBERMAN GAME - DAY 5 TEST");
        System.out.println("================================\n");

        // Database
        DatabaseManager db = DatabaseManager.getInstance();
        System.out.println();

        // ============ DECORATOR PATTERN TEST ============
        System.out.println("📋 TEST 1: Decorator Pattern (Power-ups)");
        System.out.println("-----------------------------------------");

        // Base player oluştur
        IPlayer player = new BasePlayer("Hero", 5, 5);
        System.out.println("Base player: " + player);
        System.out.println("  Speed: " + player.getSpeed());
        System.out.println("  Bomb Count: " + player.getBombCount());
        System.out.println("  Bomb Power: " + player.getBombPower());
        System.out.println();

        // SpeedBoost ekle
        System.out.println("Collecting SpeedBoost power-up...");
        player = new SpeedBoostDecorator(player);
        System.out.println("After power-up: " + player);
        System.out.println("  Speed: " + player.getSpeed() + " (increased!)");
        System.out.println();

        // BombPower ekle
        System.out.println("Collecting BombPower power-up...");
        player = new BombPowerDecorator(player);
        System.out.println("After power-up: " + player);
        System.out.println("  Bomb Power: " + player.getBombPower() + " (increased!)");
        System.out.println();

        // BombCount ekle
        System.out.println("Collecting BombCount power-up...");
        player = new BombCountDecorator(player);
        System.out.println("After power-up: " + player);
        System.out.println("  Bomb Count: " + player.getBombCount() + " (increased!)");
        System.out.println();

        // Tüm statları göster
        System.out.println("Final player stats:");
        System.out.println("  Speed: " + player.getSpeed());
        System.out.println("  Bomb Count: " + player.getBombCount());
        System.out.println("  Bomb Power: " + player.getBombPower());
        System.out.println();

        // Birden fazla aynı decorator
        System.out.println("Collecting 2 more SpeedBoosts...");
        player = new SpeedBoostDecorator(player);
        player = new SpeedBoostDecorator(player);
        System.out.println("Super fast player: " + player);
        System.out.println("  Speed: " + player.getSpeed() + " (VERY FAST!)");
        System.out.println();

        // ============ STATE PATTERN TEST ============
        System.out.println("📋 TEST 2: State Pattern (Game States)");
        System.out.println("---------------------------------------");

        GameStateManager stateManager = GameStateManager.getInstance();

        // Menu'den Playing'e geç
        System.out.println("\nStarting game...");
        stateManager.changeState(new PlayingState());
        stateManager.update();
        stateManager.handleInput("MOVE_UP");
        stateManager.handleInput("PLACE_BOMB");

        // Playing'den Paused'a geç
        System.out.println("\nPausing game...");
        stateManager.changeState(new PausedState());
        stateManager.handleInput("RESUME");

        // Paused'dan Playing'e geri dön
        System.out.println("\nResuming game...");
        stateManager.changeState(new PlayingState());
        stateManager.update();

        // Playing'den GameOver'a geç
        System.out.println("\nGame ended...");
        stateManager.changeState(new GameOverState("Player 1"));
        stateManager.handleInput("REMATCH");

        // GameOver'dan Menu'ye dön
        System.out.println("\nReturning to menu...");
        stateManager.changeState(new MenuState());

        // Database kapat
        System.out.println();
        db.closeConnection();

        System.out.println("\n================================");
        System.out.println("✅ Day 5 Complete! 🎉\n");

        System.out.println("📊 PATTERN PROGRESS:");
        System.out.println("✅ Singleton Pattern (DatabaseManager, GameStateManager)");
        System.out.println("✅ Repository Pattern (UserRepository)");
        System.out.println("✅ Factory Pattern (Wall, PowerUp, Enemy)");
        System.out.println("✅ Strategy Pattern (Enemy AI Behaviors)");
        System.out.println("✅ Observer Pattern (Game Events)");
        System.out.println("✅ Decorator Pattern (Player Power-ups)");
        System.out.println("✅ State Pattern (Game State Machine)");
        System.out.println("⬜ MVC Architecture");
        System.out.println("\n🎯 7/8 Patterns Complete! (87.5%)");
        System.out.println("🔥 Almost there! Just MVC Architecture left!");
    }
}