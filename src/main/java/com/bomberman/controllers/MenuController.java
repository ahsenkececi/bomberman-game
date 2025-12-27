package com.bomberman.controllers;

import com.bomberman.models.*;
import com.bomberman.repository.*;
import com.bomberman.views.MainWindow;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.JOptionPane;

public class MenuController {

    private UserRepository userRepository;
    private GameStatsRepository statsRepository;
    private MainWindow mainWindow;

    public MenuController(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.userRepository = new UserRepository();
        this.statsRepository = new GameStatsRepository();
    }

    // Kullanıcı girişi
    public boolean login(String username, String password) {
        User user = userRepository.getByUsername(username);

        if (user == null) {
            System.out.println("❌ User not found: " + username);
            return false;
        }

        String hashedPassword = hashPassword(password);
        if (user.getPasswordHash().equals(hashedPassword)) {
            // Giriş başarılı
            GameManager.getInstance().setLoggedInUser(user);

            // Tema yükle
            String savedTheme = user.getPreferredTheme();
            if (savedTheme != null && !savedTheme.isEmpty()) {
                GameManager.getInstance().setSelectedTheme(Theme.valueOf(savedTheme));
            }

            System.out.println("✅ Login successful: " + username);
            return true;
        }

        System.out.println("❌ Wrong password for user: " + username);
        return false;
    }

    // Yeni kullanıcı kaydı
    public boolean register(String username, String password) {
        // Kullanıcı zaten var mı?
        if (userRepository.getByUsername(username) != null) {
            System.out.println("❌ Username already exists: " + username);
            return false;
        }

        String hashedPassword = hashPassword(password);
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPasswordHash(hashedPassword);
        newUser.setPreferredTheme("FOREST"); // Default tema

        userRepository.add(newUser);

        // Yeni kullanıcı için stats başlat
        User savedUser = userRepository.getByUsername(username);
        if (savedUser != null) {
            statsRepository.initializeStats(savedUser.getId());
            System.out.println("✅ Registration successful: " + username);
            return true;
        }

        return false;
    }

    // ✅ Local Multiplayer - GameController ile oyunu başlatır
    public void startGame(Theme theme, boolean multiplayer) {
        User user = GameManager.getInstance().getLoggedInUser();

        if (user != null) {
            GameManager.getInstance().startNewGame(user, theme, multiplayer);

            // MainWindow'u gizle
            mainWindow.setVisible(false);

            // ✅ YENİ: Separate thread'de oyunu başlat
            new Thread(() -> {
                GameController controller = new GameController();
                controller.run();

                // Oyun bitince MainWindow'u tekrar göster
                javax.swing.SwingUtilities.invokeLater(() -> {
                    mainWindow.setVisible(true);
                });
            }).start();

        } else {
            JOptionPane.showMessageDialog(null, "❌ Please login first!");
        }
    }

    // ✅ Online Host olarak başlat
    public void startGameAsHost(Theme theme) {
        User user = GameManager.getInstance().getLoggedInUser();

        if (user != null) {
            GameManager.getInstance().startNewGame(user, theme, true);

            // MainWindow'u gizle
            mainWindow.setVisible(false);

            // ✅ YENİ: Separate thread'de host olarak başlat
            new Thread(() -> {
                GameController controller = new GameController();
                controller.startAsHost(); // Online host başlat

                // Oyun bitince MainWindow'u tekrar göster
                javax.swing.SwingUtilities.invokeLater(() -> {
                    mainWindow.setVisible(true);
                });
            }).start();

            // Bilgilendirme
            JOptionPane.showMessageDialog(
                    null,
                    "🌐 Server Starting!\n\n" +
                            "Waiting for other player to connect...\n" +
                            "Your IP: Check console output",
                    "Host Mode",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } else {
            JOptionPane.showMessageDialog(null, "❌ Please login first!");
        }
    }

    // ✅ Online Client olarak başlat
    public void startGameAsClient(Theme theme, String serverIp) {
        User user = GameManager.getInstance().getLoggedInUser();

        if (user != null) {
            GameManager.getInstance().startNewGame(user, theme, true);

            // MainWindow'u gizle
            mainWindow.setVisible(false);

            // ✅ YENİ: Separate thread'de client olarak başlat
            new Thread(() -> {
                GameController controller = new GameController();
                controller.startAsClient(serverIp); // Online client başlat

                // Oyun bitince MainWindow'u tekrar göster
                javax.swing.SwingUtilities.invokeLater(() -> {
                    mainWindow.setVisible(true);
                });
            }).start();

        } else {
            JOptionPane.showMessageDialog(null, "❌ Please login first!");
        }
    }

    // Tema seçimi
    public void selectTheme(Theme theme) {
        GameManager.getInstance().setSelectedTheme(theme);

        User user = GameManager.getInstance().getLoggedInUser();
        if (user != null) {
            userRepository.updateTheme(user.getId(), theme.name());
            System.out.println("✅ Theme selected: " + theme);
        }
    }

    // Çıkış
    public void logout() {
        GameManager.getInstance().setLoggedInUser(null);
        System.out.println("✅ User logged out");
    }

    // Şifre hashleme (MD5)
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password; // Fallback
        }
    }
}