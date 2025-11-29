package com.bomberman;

import com.bomberman.database.DatabaseManager;
import com.bomberman.models.User;
import com.bomberman.repository.UserRepository;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("🎮 BOMBERMAN GAME - DAY 2 TEST");
        System.out.println("================================\n");

        // Database bağlantısı
        DatabaseManager db = DatabaseManager.getInstance();
        System.out.println();

        // Repository oluştur
        UserRepository userRepo = new UserRepository();

        // TEST 1: Kullanıcı Ekle
        System.out.println("📋 TEST 1: Adding Users...");
        User user1 = new User("player1", "hashed_password_123");
        User user2 = new User("player2", "hashed_password_456");
        User user3 = new User("admin", "hashed_password_789");

        userRepo.add(user1);
        userRepo.add(user2);
        userRepo.add(user3);
        System.out.println();

        // TEST 2: Tüm Kullanıcıları Listele
        System.out.println("📋 TEST 2: Getting All Users...");
        List<User> allUsers = userRepo.getAll();
        System.out.println("Total users: " + allUsers.size());
        for (User user : allUsers) {
            System.out.println("  - " + user);
        }
        System.out.println();

        // TEST 3: ID'ye Göre Kullanıcı Getir
        System.out.println("📋 TEST 3: Getting User by ID...");
        User foundUser = userRepo.getById(1);
        if (foundUser != null) {
            System.out.println("✅ Found: " + foundUser);
        } else {
            System.out.println("❌ User not found!");
        }
        System.out.println();

        // TEST 4: Username'e Göre Kullanıcı Getir
        System.out.println("📋 TEST 4: Getting User by Username...");
        User foundByUsername = userRepo.getByUsername("player1");
        if (foundByUsername != null) {
            System.out.println("✅ Found: " + foundByUsername);
        } else {
            System.out.println("❌ User not found!");
        }
        System.out.println();

        // TEST 5: Kullanıcı Güncelle
        System.out.println("📋 TEST 5: Updating User...");
        if (foundUser != null) {
            foundUser.setUsername("player1_updated");
            userRepo.update(foundUser);
        }
        System.out.println();

        // TEST 6: Güncellenmiş Kullanıcıyı Kontrol Et
        System.out.println("📋 TEST 6: Verify Update...");
        User updatedUser = userRepo.getById(1);
        if (updatedUser != null) {
            System.out.println("✅ Updated user: " + updatedUser);
        }
        System.out.println();

        // Database bağlantısını kapat
        db.closeConnection();

        System.out.println("================================");
        System.out.println("✅ Day 2 Complete! 🎉");
        System.out.println("\n📊 PATTERN PROGRESS:");
        System.out.println("✅ Singleton Pattern (DatabaseManager)");
        System.out.println("✅ Repository Pattern (UserRepository)");
        System.out.println("⬜ Factory Pattern");
        System.out.println("⬜ Strategy Pattern");
        System.out.println("⬜ Observer Pattern");
        System.out.println("⬜ Decorator Pattern");
        System.out.println("⬜ State Pattern");
        System.out.println("⬜ MVC Architecture");
    }
}




