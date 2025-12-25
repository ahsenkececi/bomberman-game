package com.bomberman.views;

import com.bomberman.controllers.MenuController;
import com.bomberman.models.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuView extends JPanel {

    private MenuController controller;

    // Components
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel messageLabel;
    private JComboBox<Theme> themeComboBox;
    private JButton startGameButton;
    private JButton leaderboardButton;
    private JPanel loginPanel;
    private JPanel mainMenuPanel;

    public MenuView(MenuController controller) {
        this.controller = controller;
        setLayout(new CardLayout());
        setBackground(new Color(34, 40, 49));

        // Login/Register paneli
        loginPanel = createLoginPanel();

        // Ana menü paneli (giriş yaptıktan sonra)
        mainMenuPanel = createMainMenuPanel();

        add(loginPanel, "LOGIN");
        add(mainMenuPanel, "MAIN_MENU");

        // İlk ekran login
        showLogin();
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(34, 40, 49));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("💣 BOMBERMAN GAME 💣");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(238, 238, 238));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);

        // Username label
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(usernameLabel, gbc);

        // Username field
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(usernameField, gbc);

        // Password label
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(passwordLabel, gbc);

        // Password field
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(passwordField, gbc);

        // Buttons panel
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(34, 40, 49));

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(0, 173, 181));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(238, 82, 83));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegister();
            }
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        panel.add(buttonPanel, gbc);

        // Message label
        gbc.gridy = 4;
        messageLabel = new JLabel(" ");
        messageLabel.setForeground(Color.RED);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(messageLabel, gbc);

        return panel;
    }

    private JPanel createMainMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(34, 40, 49));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome, Player!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(238, 238, 238));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(welcomeLabel, gbc);

        // Theme selection
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel themeLabel = new JLabel("Select Theme:");
        themeLabel.setForeground(Color.WHITE);
        themeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(themeLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        themeComboBox = new JComboBox<>(Theme.values());
        themeComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        themeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Theme selected = (Theme) themeComboBox.getSelectedItem();
                controller.selectTheme(selected);
            }
        });
        panel.add(themeComboBox, gbc);

        // Start Game button
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        startGameButton = new JButton("🎮 Start Game");
        startGameButton.setFont(new Font("Arial", Font.BOLD, 18));
        startGameButton.setBackground(new Color(0, 173, 181));
        startGameButton.setForeground(Color.WHITE);
        startGameButton.setFocusPainted(false);
        startGameButton.setPreferredSize(new Dimension(250, 50));
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Theme selected = (Theme) themeComboBox.getSelectedItem();
                controller.startGame(selected, false); // Single player
            }
        });
        panel.add(startGameButton, gbc);

        // Leaderboard button
        gbc.gridy = 3;
        leaderboardButton = new JButton("🏆 Leaderboard");
        leaderboardButton.setFont(new Font("Arial", Font.BOLD, 18));
        leaderboardButton.setBackground(new Color(253, 203, 110));
        leaderboardButton.setForeground(new Color(34, 40, 49));
        leaderboardButton.setFocusPainted(false);
        leaderboardButton.setPreferredSize(new Dimension(250, 50));
        leaderboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Window window = SwingUtilities.getWindowAncestor(MenuView.this);
                if (window instanceof com.bomberman.views.MainWindow) {  // ✅ MainWindow
                    ((com.bomberman.views.MainWindow) window).showLeaderboard();
                }
            }
        });
        panel.add(leaderboardButton, gbc);

        // Logout button
        gbc.gridy = 4;
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 14));
        logoutButton.setBackground(new Color(238, 82, 83));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.logout();
                showLogin();
                clearFields();
            }
        });
        panel.add(logoutButton, gbc);

        return panel;
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("⚠️ Please fill all fields!");
            messageLabel.setForeground(Color.ORANGE);
            return;
        }

        boolean success = controller.login(username, password);

        if (success) {
            messageLabel.setText("✅ Login successful!");
            messageLabel.setForeground(Color.GREEN);
            showMainMenu();
            clearFields();
        } else {
            messageLabel.setText("❌ Invalid username or password!");
            messageLabel.setForeground(Color.RED);
        }
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("⚠️ Please fill all fields!");
            messageLabel.setForeground(Color.ORANGE);
            return;
        }

        if (password.length() < 4) {
            messageLabel.setText("⚠️ Password must be at least 4 characters!");
            messageLabel.setForeground(Color.ORANGE);
            return;
        }

        boolean success = controller.register(username, password);

        if (success) {
            messageLabel.setText("✅ Registration successful! Please login.");
            messageLabel.setForeground(Color.GREEN);
            clearFields();
        } else {
            messageLabel.setText("❌ Username already exists!");
            messageLabel.setForeground(Color.RED);
        }
    }

    private void showLogin() {
        CardLayout cl = (CardLayout) getLayout();
        cl.show(this, "LOGIN");
    }

    private void showMainMenu() {
        CardLayout cl = (CardLayout) getLayout();
        cl.show(this, "MAIN_MENU");
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        messageLabel.setText(" ");
    }
}