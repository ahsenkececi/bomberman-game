package com.bomberman;

import com.bomberman.database.DatabaseManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("🎮 BOMBERMAN GAME - DAY 1 TEST");
        System.out.println("================================\n");

        // SINGLETON PATTERN TEST
        System.out.println("Testing Singleton Pattern...");
        DatabaseManager db1 = DatabaseManager.getInstance();
        DatabaseManager db2 = DatabaseManager.getInstance();

        System.out.println("Instance 1 hash: " + db1.hashCode());
        System.out.println("Instance 2 hash: " + db2.hashCode());
        System.out.println("Are they same? " + (db1 == db2));

        if (db1 == db2) {
            System.out.println("✅ SINGLETON PATTERN WORKING!\n");
        } else {
            System.out.println("❌ Singleton failed!\n");
        }

        // Database bağlantısını kapat
        db1.closeConnection();

        System.out.println("================================");
        System.out.println("Day 1 Complete! 🎉");
    }
}
