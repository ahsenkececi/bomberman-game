package com.bomberman.views;

import com.bomberman.controllers.MenuController;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private MenuView menuView;
    private LeaderboardView leaderboardView;

    private MenuController menuController;

    public MainWindow() {
        setTitle("💣 Bomberman - Main Menu");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // CardLayout ile ekranlar arası geçiş
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Controller oluştur
        menuController = new MenuController(this);

        // View'ları ekle
        menuView = new MenuView(menuController);
        leaderboardView = new LeaderboardView();

        // Leaderboard'un back butonuna action ekle
        leaderboardView.getBackButton().addActionListener(e -> showMenu());

        mainPanel.add(menuView, "MENU");
        mainPanel.add(leaderboardView, "LEADERBOARD");

        add(mainPanel);

        // İlk ekran menü
        showMenu();
    }

    public void showMenu() {
        cardLayout.show(mainPanel, "MENU");
    }

    public void showLeaderboard() {
        leaderboardView.refresh();
        cardLayout.show(mainPanel, "LEADERBOARD");
    }

    // Oyun başlatma - GameWindow'u aç
    public void startGame() {
        // MainWindow'u gizle
        this.setVisible(false);

        // GameWindow'u başlat (sizin eski kodunuz)
        // InputController oluştur
        com.bomberman.controllers.InputController inputController =
                new com.bomberman.controllers.InputController();

        GameWindow gameWindow = new GameWindow(inputController);

        // GameWindow kapandığında MainWindow'a dön
        gameWindow.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                MainWindow.this.setVisible(true);
            }
        });
    }

    // Main method (test için)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}