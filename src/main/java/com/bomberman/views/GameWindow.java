package com.bomberman.views;

import com.bomberman.controllers.GameController;
import com.bomberman.controllers.InputController;
import com.bomberman.models.*;
import com.bomberman.theme.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.geom.*;

public class GameWindow extends JFrame {

    private GamePanel gamePanel;
    private InputController inputController;

    private static final int WINDOW_WIDTH = 750;  // 15 cells * 50px
    private static final int WINDOW_HEIGHT = 650; // 13 cells * 50px

    public GameWindow(InputController inputController) {
        this.inputController = inputController;

        setTitle("💣 Bomberman Game");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT+80);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null); // Center screen

        // Game panel oluştur
        gamePanel = new GamePanel();
        add(gamePanel);

        // Input controller'ı ekle
        addKeyListener(inputController);

        setVisible(true);

        System.out.println("✅ Game window created!");
    }

    // Oyunu render et
    public void render(Map map, List<Player> players, List<Bomb> bombs, List<Enemy> enemies, List<Explosion> explosions, List<Notification> notifications, List<PowerUp> powerUps) {
        gamePanel.updateGameState(map, players, bombs, enemies, explosions, notifications, powerUps);
        gamePanel.repaint();
    }

    // İç panel (çizim için)
    class GamePanel extends JPanel {

        private Map map;
        private List<Player> players;
        private List<Bomb> bombs;
        private List<Enemy> enemies;
        private List<Explosion> explosions;
        private List<Notification> notifications;
        private List<PowerUp> powerUps;

        private static final int CELL_SIZE = 50;

        public GamePanel() {
            setBackground(Color.DARK_GRAY);
            setFocusable(false);
        }

        public void updateGameState(Map map, List<Player> players, List<Bomb> bombs, List<Enemy> enemies, List<Explosion> explosions, List<Notification> notifications, List<PowerUp> powerUps) {
            this.map = map;
            this.players = players;
            this.bombs = bombs;
            this.enemies = enemies;
            this.explosions = explosions;
            this.notifications = notifications;
            this.powerUps = powerUps;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (map == null) return;

            // Haritayı çiz
            drawMap(g);

            // Bombaları çiz
            if (bombs != null) {
                drawBombs(g);
            }

            // Power-up'ları çiz (haritadan sonra, oyunculardan önce)
            if (powerUps != null) {
                drawPowerUps(g);
            }

            // Düşmanları çiz
            if (enemies != null) {
                drawEnemies(g);
            }

            // Oyuncuları çiz
            if (players != null) {
                drawPlayers(g);
            }
            if (explosions != null) {
                drawExplosions(g);
            }
            if (notifications != null) {
                drawNotifications(g);
            }

            // HUD çiz
            drawHUD(g);
        }

        private void drawMap(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            ThemeManager themeManager = ThemeManager.getInstance();
            Theme theme = themeManager.getCurrentTheme();

            for (int y = 0; y < map.getHeight(); y++) {
                for (int x = 0; x < map.getWidth(); x++) {
                    int cellValue = map.getCell(x, y);
                    int screenX = x * CELL_SIZE;
                    int screenY = y * CELL_SIZE;

                    switch (cellValue) {
                        case 0: // Empty
                            drawGround(g2d, screenX, screenY, x, y, theme);
                            break;

                        case 1: // Unbreakable
                            drawUnbreakableWall(g2d, screenX, screenY, theme);
                            break;

                        case 2: // Breakable
                            drawBreakableWall(g2d, screenX, screenY, theme);
                            break;

                        case 3: // Hard
                            drawHardWall(g2d, screenX, screenY, theme);
                            break;
                    }

                    // Grid çizgileri (hafif)
                    g.setColor(new Color(0, 0, 0, 30));
                    g2d.setStroke(new BasicStroke(1));
                    g.drawRect(screenX, screenY, CELL_SIZE, CELL_SIZE);
                }
            }
        }
            // ========== ZEMİN ÇİZİMLERİ ==========

            private void drawGround(Graphics2D g2d, int x, int y, int gridX, int gridY, Theme theme) {
                switch (theme) {
                    case DESERT:
                        drawDesertGround(g2d, x, y, gridX, gridY);
                        break;
                    case FOREST:
                        drawForestGround(g2d, x, y, gridX, gridY);
                        break;
                    case CITY:
                        drawCityGround(g2d, x, y, gridX, gridY);
                        break;
                }
            }

// DESERT - Kumsal zemin
            private void drawDesertGround(Graphics2D g2d, int x, int y, int gridX, int gridY) {
                // Ana kum rengi
                if ((gridX + gridY) % 2 == 0) {
                    g2d.setColor(new Color(237, 201, 175));
                } else {
                    g2d.setColor(new Color(210, 180, 140));
                }
                g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                // Kum taneleri efekti (random noktalar)
                g2d.setColor(new Color(194, 178, 128, 80));
                for (int i = 0; i < 5; i++) {
                    int dotX = x + (gridX * 7 + i * 11) % CELL_SIZE;
                    int dotY = y + (gridY * 13 + i * 7) % CELL_SIZE;
                    g2d.fillOval(dotX, dotY, 2, 2);
                }
            }

// FOREST - Çimen zemin
            private void drawForestGround(Graphics2D g2d, int x, int y, int gridX, int gridY) {
                // Çim rengi
                if ((gridX + gridY) % 2 == 0) {
                    g2d.setColor(new Color(34, 139, 34));
                } else {
                    g2d.setColor(new Color(50, 155, 50));
                }
                g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                // Çim çizgileri
                g2d.setColor(new Color(20, 100, 20, 100));
                for (int i = 0; i < 4; i++) {
                    int lineX = x + (gridX * 5 + i * 13) % CELL_SIZE;
                    int lineY = y + (gridY * 7 + i * 11) % CELL_SIZE;
                    g2d.drawLine(lineX, lineY, lineX + 5, lineY + 3);
                }
            }

// CITY - Asfalt zemin
            private void drawCityGround(Graphics2D g2d, int x, int y, int gridX, int gridY) {
                // Asfalt rengi
                if ((gridX + gridY) % 2 == 0) {
                    g2d.setColor(new Color(80, 80, 80));
                } else {
                    g2d.setColor(new Color(100, 100, 100));
                }
                g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                // Asfalt çatlakları
                g2d.setColor(new Color(60, 60, 60));
                if ((gridX + gridY) % 5 == 0) {
                    g2d.drawLine(x, y + CELL_SIZE/2, x + CELL_SIZE, y + CELL_SIZE/2 + 5);
                }
            }


        // ========== UNBREAKABLE WALL ÇİZİMLERİ ==========

        private void drawUnbreakableWall(Graphics2D g2d, int x, int y, Theme theme) {
            switch (theme) {
                case DESERT:
                    drawDesertUnbreakable(g2d, x, y);
                    break;
                case FOREST:
                    drawForestUnbreakable(g2d, x, y);
                    break;
                case CITY:
                    drawCityUnbreakable(g2d, x, y);
                    break;
            }
        }

        private void drawDesertUnbreakable(Graphics2D g2d, int x, int y) {
            g2d.setColor(new Color(139, 90, 43));
            g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);

            g2d.setColor(new Color(160, 100, 50));
            g2d.fillRect(x + 5, y + 5, CELL_SIZE - 10, 10);
            g2d.fillRect(x + 5, y + 20, CELL_SIZE - 10, 10);
            g2d.fillRect(x + 5, y + 35, CELL_SIZE - 10, 10);

            g2d.setColor(new Color(80, 50, 20));
            g2d.setStroke(new BasicStroke(4));
            g2d.drawRect(x, y, CELL_SIZE, CELL_SIZE);

            g2d.setColor(new Color(200, 150, 100, 80));
            g2d.fillRect(x, y, 15, 15);
        }

        private void drawForestUnbreakable(Graphics2D g2d, int x, int y) {
            g2d.setColor(new Color(40, 80, 40));
            g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);

            g2d.setColor(new Color(50, 100, 50));
            for (int i = 0; i < CELL_SIZE; i += 8) {
                g2d.fillRect(x + 3, y + i, CELL_SIZE - 6, 4);
            }

            g2d.setColor(new Color(60, 120, 60));
            g2d.fillOval(x + 10, y + 10, 30, 30);
            g2d.setColor(new Color(40, 80, 40));
            g2d.fillOval(x + 15, y + 15, 20, 20);

            g2d.setColor(new Color(20, 40, 20));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(x, y, CELL_SIZE, CELL_SIZE);
        }
        private void drawCityUnbreakable(Graphics2D g2d, int x, int y) {
            g2d.setColor(new Color(60, 60, 60));
            g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);

            g2d.setColor(new Color(80, 80, 80));
            for (int i = 0; i < 3; i++) {
                g2d.drawLine(x + 10 + i * 15, y, x + 10 + i * 15, y + CELL_SIZE);
            }

            g2d.setColor(new Color(100, 100, 100));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);

            g2d.fillOval(x + 3, y + 3, 6, 6);
            g2d.fillOval(x + CELL_SIZE - 9, y + 3, 6, 6);
            g2d.fillOval(x + 3, y + CELL_SIZE - 9, 6, 6);
            g2d.fillOval(x + CELL_SIZE - 9, y + CELL_SIZE - 9, 6, 6);

            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(x, y, CELL_SIZE, CELL_SIZE);
        }

        // ========== BREAKABLE WALL ÇİZİMLERİ ==========

        private void drawBreakableWall(Graphics2D g2d, int x, int y, Theme theme) {
            switch (theme) {
                case DESERT:
                    drawDesertBreakable(g2d, x, y);
                    break;
                case FOREST:
                    drawForestBreakable(g2d, x, y);
                    break;
                case CITY:
                    drawCityBreakable(g2d, x, y);
                    break;
            }
        }

        private void drawDesertBreakable(Graphics2D g2d, int x, int y) {
            g2d.setColor(new Color(218, 165, 32));
            g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);

            g2d.setColor(new Color(184, 134, 11));
            for (int i = 0; i < 5; i++) {
                g2d.drawLine(x, y + i * 10, x + CELL_SIZE, y + i * 10);
            }

            g2d.setColor(new Color(160, 120, 20));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(x, y, CELL_SIZE, CELL_SIZE);

            g2d.setColor(new Color(139, 90, 43));
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            g2d.drawString("≈", x + 15, y + 32);
        }

        private void drawForestBreakable(Graphics2D g2d, int x, int y) {
            g2d.setColor(new Color(101, 67, 33));
            g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);

            g2d.setColor(new Color(139, 90, 43));
            for (int i = 0; i < 4; i++) {
                g2d.fillRect(x + i * 12, y, 8, CELL_SIZE);
            }

            g2d.setColor(new Color(70, 70, 70));
            g2d.fillRect(x + 5, y + 5, CELL_SIZE - 10, 4);
            g2d.fillRect(x + 5, y + CELL_SIZE - 9, CELL_SIZE - 10, 4);

            g2d.setColor(new Color(40, 40, 40));
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.drawString("✕", x + 16, y + 32);

            g2d.setColor(new Color(60, 40, 20));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(x, y, CELL_SIZE, CELL_SIZE);
        }

        private void drawCityBreakable(Graphics2D g2d, int x, int y) {
            g2d.setColor(new Color(139, 90, 43));
            g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);

            g2d.setColor(new Color(160, 100, 50));
            g2d.fillRect(x + 2, y + 2, 20, 10);
            g2d.fillRect(x + 25, y + 2, 20, 10);
            g2d.fillRect(x + 2, y + 15, 15, 10);
            g2d.fillRect(x + 20, y + 15, 25, 10);
            g2d.fillRect(x + 2, y + 28, 20, 10);
            g2d.fillRect(x + 25, y + 28, 20, 10);

            g2d.setColor(new Color(80, 50, 30));
            g2d.setStroke(new BasicStroke(1));
            g2d.drawLine(x, y + 12, x + CELL_SIZE, y + 12);
            g2d.drawLine(x, y + 25, x + CELL_SIZE, y + 25);
            g2d.drawLine(x, y + 38, x + CELL_SIZE, y + 38);
            g2d.drawLine(x + 22, y, x + 22, y + CELL_SIZE);

            g2d.setColor(new Color(100, 60, 30));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawLine(x + 15, y, x + 20, y + CELL_SIZE);

            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(x, y, CELL_SIZE, CELL_SIZE);
        }

        // ========== HARD WALL ÇİZİMLERİ ==========

        private void drawHardWall(Graphics2D g2d, int x, int y, Theme theme) {
            switch (theme) {
                case DESERT:
                    drawDesertHard(g2d, x, y);
                    break;
                case FOREST:
                    drawForestHard(g2d, x, y);
                    break;
                case CITY:
                    drawCityHard(g2d, x, y);
                    break;
            }
        }
        private void drawDesertHard(Graphics2D g2d, int x, int y) {
            GradientPaint gradient = new GradientPaint(
                    x, y, new Color(205, 92, 92),
                    x + CELL_SIZE, y + CELL_SIZE, new Color(139, 0, 0)
            );
            g2d.setPaint(gradient);
            g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);

            g2d.setColor(new Color(255, 160, 122, 150));
            int[] xPoints = {x + 10, x + 25, x + 15};
            int[] yPoints = {y + 10, y + 10, y + 30};
            g2d.fillPolygon(xPoints, yPoints, 3);

            g2d.fillPolygon(new int[]{x + 30, x + 40, x + 35}, new int[]{y + 15, y + 25, y + 40}, 3);

            g2d.setColor(new Color(100, 0, 0));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(x, y, CELL_SIZE, CELL_SIZE);

            g2d.setColor(Color.YELLOW);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString("III", x + 16, y + 44);
        }
        private void drawForestHard(Graphics2D g2d, int x, int y) {
            GradientPaint gradient = new GradientPaint(
                    x, y, new Color(85, 107, 47),
                    x + CELL_SIZE, y + CELL_SIZE, new Color(107, 142, 35)
            );
            g2d.setPaint(gradient);
            g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);

            g2d.setColor(new Color(60, 80, 30));
            g2d.setStroke(new BasicStroke(3));
            for (int i = 0; i < 4; i++) {
                g2d.drawLine(x + 5, y + i * 12, x + CELL_SIZE - 5, y + i * 12 + 3);
            }

            g2d.setColor(new Color(40, 60, 20));
            g2d.fillOval(x + 5, y + 15, 12, 12);
            g2d.fillOval(x + 33, y + 25, 12, 12);

            g2d.setColor(new Color(30, 50, 15));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(x, y, CELL_SIZE, CELL_SIZE);

            g2d.setColor(new Color(144, 238, 144));
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString("III", x + 16, y + 44);
        }

        private void drawCityHard(Graphics2D g2d, int x, int y) {
            GradientPaint gradient = new GradientPaint(
                    x, y, new Color(140, 140, 140),
                    x + CELL_SIZE, y + CELL_SIZE, new Color(80, 80, 80)
            );
            g2d.setPaint(gradient);
            g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);

            g2d.setColor(new Color(100, 100, 100));
            g2d.fillRect(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);

            g2d.setColor(new Color(60, 60, 60));
            g2d.fillOval(x + 5, y + 5, 8, 8);
            g2d.fillOval(x + CELL_SIZE - 13, y + 5, 8, 8);
            g2d.fillOval(x + 5, y + CELL_SIZE - 13, 8, 8);
            g2d.fillOval(x + CELL_SIZE - 13, y + CELL_SIZE - 13, 8, 8);

            g2d.setStroke(new BasicStroke(1));
            for (int i = 0; i < 4; i++) {
                int vx = (i % 2 == 0) ? x + 9 : x + CELL_SIZE - 9;
                int vy = (i < 2) ? y + 9 : y + CELL_SIZE - 9;
                g2d.drawLine(vx - 2, vy, vx + 2, vy);
            }
            g2d.setColor(new Color(255, 215, 0));
            g2d.setStroke(new BasicStroke(2));
            for (int i = 0; i < 3; i++) {
                g2d.drawLine(x + 15, y + 15 + i * 10, x + 35, y + 15 + i * 10);
            }

            g2d.setColor(new Color(50, 50, 50));
            g2d.setStroke(new BasicStroke(4));
            g2d.drawRect(x, y, CELL_SIZE, CELL_SIZE);

            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString("III", x + 16, y + 44);
        }



        private void drawPowerUps(Graphics g) {
            for (PowerUp powerUp : powerUps) {
                int screenX = powerUp.getX() * CELL_SIZE;
                int screenY = powerUp.getY() * CELL_SIZE;

                //Animasyon (yukari asagi hareket)
                float offset = (float) Math.sin(powerUp.getAnimationTimer()) * 5;
                screenY += (int) offset;

                // Parlaklık animasyonu
                float brightness = 0.8f + (float) Math.sin(powerUp.getAnimationTimer() * 2) * 0.2f;

                // Power-up tipine göre renk ve şekil
                if (powerUp instanceof SpeedBoostPowerUp) {
                    Color color = new Color(0, (int) (255 * brightness), 255);
                    drawStar(g, screenX + CELL_SIZE / 2, screenY + CELL_SIZE / 2, 15, color, "⚡");
                } else if (powerUp instanceof BombPowerPowerUp) {
                    Color color = new Color(255, (int) (140 * brightness), 0);
                    drawStar(g, screenX + CELL_SIZE / 2, screenY + CELL_SIZE / 2, 15, color, "💥");
                } else if (powerUp instanceof BombCountPowerUp) {
                    Color color = new Color(0, (int) (255 * brightness), 0);
                    drawStar(g, screenX + CELL_SIZE / 2, screenY + CELL_SIZE / 2, 15, color, "💣");
                } else if (powerUp instanceof ShieldPowerUp) {
                    Color color = new Color((int) (100 * brightness), (int) (150 * brightness), 255);
                    drawStar(g, screenX + CELL_SIZE / 2, screenY + CELL_SIZE / 2, 15, color, "🛡");
                } else if (powerUp instanceof GhostPowerUp) {
                    Color color = new Color((int) (200 * brightness), (int) (200 * brightness), (int) (255 * brightness));
                    drawStar(g, screenX + CELL_SIZE / 2, screenY + CELL_SIZE / 2, 15, color, "👻");
                }
            }
        }

        // Yıldız şekli çiz (power-up için)
        private void drawStar(Graphics g, int centerX, int centerY, int size, Color color, String emoji) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
            g2d.fillOval(centerX - size - 5, centerY - size - 5, (size + 5) * 2, (size + 5) * 2);

            g2d.setColor(color);
            g2d.fillOval(centerX - size, centerY - size, size * 2, size * 2);

            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(centerX - size, centerY - size, size * 2, size * 2);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
            FontMetrics fm = g2d.getFontMetrics();
            int emojiWidth = fm.stringWidth(emoji);
            g2d.drawString(emoji, centerX - emojiWidth / 2, centerY + 6);
        }

        private void drawPlayers(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);

                if (!player.isAlive()) continue;

                int screenX = player.getX() * CELL_SIZE;
                int screenY = player.getY() * CELL_SIZE;

                // Player renkleri
                Color bodyColor = (i == 0) ? new Color(100, 150, 255) : new Color(255, 100, 100);
                Color darkColor = (i == 0) ? new Color(50, 100, 200) : new Color(200, 50, 50);

                // ========== GÖVDE (Kafa) ==========
                g2d.setColor(bodyColor);
                g2d.fillOval(screenX + 12, screenY + 10, CELL_SIZE - 24, CELL_SIZE - 20);

                // Gölge
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.fillOval(screenX + 14, screenY + 12, CELL_SIZE - 28, CELL_SIZE - 24);

                // ========== GÖZLER ==========
                g2d.setColor(Color.WHITE);
                // Sol göz
                g2d.fillOval(screenX + 18, screenY + 18, 6, 8);
                // Sağ göz
                g2d.fillOval(screenX + 26, screenY + 18, 6, 8);

                // Göz bebekleri
                g2d.setColor(Color.BLACK);
                g2d.fillOval(screenX + 20, screenY + 21, 3, 4);
                g2d.fillOval(screenX + 28, screenY + 21, 3, 4);

                // ========== AĞIZ ==========
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawArc(screenX + 18, screenY + 24, 14, 8, 180, 180);

                // ========== AYAKLAR ==========
                g2d.setColor(darkColor);
                // Sol ayak
                g2d.fillOval(screenX + 10, screenY + 32, 8, 10);
                // Sağ ayak
                g2d.fillOval(screenX + 32, screenY + 32, 8, 10);

                // ========== ELLER ==========
                // Sol el
                g2d.fillOval(screenX + 8, screenY + 20, 6, 6);
                // Sağ el
                g2d.fillOval(screenX + 36, screenY + 20, 6, 6);

                // ========== KENARLK (Outline) ==========
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawOval(screenX + 12, screenY + 10, CELL_SIZE - 24, CELL_SIZE - 20);

                // ========== PLAYER NUMARASI (Küçük badge) ==========
                g2d.setColor(Color.WHITE);
                g2d.fillOval(screenX + 32, screenY + 8, 12, 12);
                g2d.setColor(darkColor);
                g2d.fillOval(screenX + 33, screenY + 9, 10, 10);
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 10));
                g2d.drawString(String.valueOf(i + 1), screenX + 36, screenY + 17);
            }
        }



        private void drawBombs(Graphics g) {
            for (Bomb bomb : bombs) {
                if (bomb.isExploded()) continue;

                int screenX = bomb.getX() * CELL_SIZE;
                int screenY = bomb.getY() * CELL_SIZE;

                // Bomba (siyah daire)
                g.setColor(Color.BLACK);
                g.fillOval(screenX + 10, screenY + 10, CELL_SIZE - 20, CELL_SIZE - 20);

                // Fitil (kırmızı)
                g.setColor(Color.RED);
                g.fillRect(screenX + 20, screenY + 5, 10, 10);
            }
        }

        private void drawEnemies(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (Enemy enemy : enemies) {
                if (!enemy.isAlive()) continue;

                int screenX = enemy.getX() * CELL_SIZE;
                int screenY = enemy.getY() * CELL_SIZE;

                // Enemy türüne göre renk ve stil
                String behaviorName = enemy.getBehavior().getBehaviorName();

                if (behaviorName.contains("Static")) {
                    drawStaticEnemy(g2d, screenX, screenY);
                } else if (behaviorName.contains("Chasing")) {
                    drawChasingEnemy(g2d, screenX, screenY);
                } else if (behaviorName.contains("Intelligent")) {
                    drawIntelligentEnemy(g2d, screenX, screenY);
                } else {
                    // Varsayılan enemy
                    drawStaticEnemy(g2d, screenX, screenY);
                }
            }
        }

        // ========== STATIC ENEMY (Kırmızı Kızgın) ==========
        private void drawStaticEnemy(Graphics2D g2d, int x, int y) {
            Color bodyColor = new Color(200, 50, 50);
            Color darkColor = new Color(150, 20, 20);

            // Gövde
            g2d.setColor(bodyColor);
            g2d.fillOval(x + 10, y + 8, CELL_SIZE - 20, CELL_SIZE - 16);

            // Gölge
            g2d.setColor(new Color(0, 0, 0, 80));
            g2d.fillOval(x + 12, y + 10, CELL_SIZE - 24, CELL_SIZE - 20);

            // Boynuzlar (kızgın)
            g2d.setColor(darkColor);
            int[] xPoints1 = {x + 12, x + 18, x + 15};
            int[] yPoints1 = {y + 8, y + 8, y + 3};
            g2d.fillPolygon(xPoints1, yPoints1, 3);

            int[] xPoints2 = {x + 32, x + 38, x + 35};
            int[] yPoints2 = {y + 8, y + 8, y + 3};
            g2d.fillPolygon(xPoints2, yPoints2, 3);

            // Gözler (kızgın)
            g2d.setColor(Color.YELLOW);
            g2d.fillOval(x + 16, y + 18, 6, 8);
            g2d.fillOval(x + 28, y + 18, 6, 8);

            g2d.setColor(Color.RED);
            g2d.fillOval(x + 18, y + 20, 3, 5);
            g2d.fillOval(x + 30, y + 20, 3, 5);

            // Ağız (kaşlı)
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            // Çatık kaşlar
            g2d.drawLine(x + 14, y + 16, x + 20, y + 18);
            g2d.drawLine(x + 36, y + 16, x + 30, y + 18);
            // Kızgın ağız
            g2d.drawArc(x + 18, y + 32, 14, 6, 0, -180);

            // Kenarlık
            g2d.setColor(Color.BLACK);
            g2d.drawOval(x + 10, y + 8, CELL_SIZE - 20, CELL_SIZE - 16);
        }

        // ========== CHASING ENEMY (Turuncu Takipçi) ==========
        private void drawChasingEnemy(Graphics2D g2d, int x, int y) {
            Color bodyColor = new Color(255, 140, 0);
            Color darkColor = new Color(200, 100, 0);

            // Gövde
            g2d.setColor(bodyColor);
            g2d.fillOval(x + 10, y + 8, CELL_SIZE - 20, CELL_SIZE - 16);

            // Gölge
            g2d.setColor(new Color(0, 0, 0, 60));
            g2d.fillOval(x + 12, y + 10, CELL_SIZE - 24, CELL_SIZE - 20);

            // Kulaklar
            g2d.setColor(darkColor);
            g2d.fillOval(x + 6, y + 14, 10, 10);
            g2d.fillOval(x + 34, y + 14, 10, 10);

            // Gözler (büyük takip eden)
            g2d.setColor(Color.WHITE);
            g2d.fillOval(x + 15, y + 16, 8, 10);
            g2d.fillOval(x + 27, y + 16, 8, 10);

            g2d.setColor(Color.BLACK);
            g2d.fillOval(x + 18, y + 20, 4, 5);
            g2d.fillOval(x + 30, y + 20, 4, 5);

            // Ağız (endişeli)
            g2d.setStroke(new BasicStroke(2));
            g2d.drawLine(x + 18, y + 30, x + 22, y + 32);
            g2d.drawLine(x + 22, y + 32, x + 28, y + 32);
            g2d.drawLine(x + 28, y + 32, x + 32, y + 30);

            // Kenarlık
            g2d.setColor(Color.BLACK);
            g2d.drawOval(x + 10, y + 8, CELL_SIZE - 20, CELL_SIZE - 16);
        }

        // ========== INTELLIGENT ENEMY (Mor Akıllı) ==========
        private void drawIntelligentEnemy(Graphics2D g2d, int x, int y) {
            Color bodyColor = new Color(138, 43, 226);
            Color darkColor = new Color(75, 0, 130);

            // Gövde
            g2d.setColor(bodyColor);
            g2d.fillOval(x + 10, y + 8, CELL_SIZE - 20, CELL_SIZE - 16);

            // Gölge
            g2d.setColor(new Color(0, 0, 0, 70));
            g2d.fillOval(x + 12, y + 10, CELL_SIZE - 24, CELL_SIZE - 20);

            // Beyin kıvrımları (akıllı)
            g2d.setColor(darkColor);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawArc(x + 12, y + 8, 12, 10, 0, 180);
            g2d.drawArc(x + 20, y + 8, 12, 10, 0, 180);
            g2d.drawArc(x + 28, y + 8, 12, 10, 0, 180);

            // Gözler (zeki bakış)
            g2d.setColor(Color.CYAN);
            g2d.fillOval(x + 16, y + 18, 7, 9);
            g2d.fillOval(x + 27, y + 18, 7, 9);

            g2d.setColor(Color.BLUE);
            g2d.fillOval(x + 18, y + 21, 4, 5);
            g2d.fillOval(x + 29, y + 21, 4, 5);

            // Gözlük (akıllı görünüm)
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawOval(x + 15, y + 17, 8, 10);
            g2d.drawOval(x + 27, y + 17, 8, 10);
            g2d.drawLine(x + 23, y + 22, x + 27, y + 22);

            // Ağız (sırıtan)
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawArc(x + 18, y + 28, 14, 10, 180, 180);

            // Kenarlık
            g2d.drawOval(x + 10, y + 8, CELL_SIZE - 20, CELL_SIZE - 16);
        }

        private void drawExplosions(Graphics g) {
            for (Explosion explosion : explosions) {
                int bx = explosion.getX();
                int by = explosion.getY();
                int power = explosion.getPower();

                drawExplosionCell(g, bx, by);

                int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

                for (int[] dir : directions) {
                    for (int i = 1; i <= power; i++) {
                        int x = bx + (dir[0] * i);
                        int y = by + (dir[1] * i);

                        if (!map.isInBounds(x, y)) break;

                        int cell = map.getCell(x, y);
                        if (cell == 1) break;

                        drawExplosionCell(g, x, y);

                        if (cell == 2 || cell == 3) break;
                    }
                }
            }
        }

        private void drawHUD(Graphics g) {
                // Arka plan (daha yüksek)
                g.setColor(new Color(0, 0, 0, 180));
                g.fillRect(0, 0, getWidth(), 80);  // 50'den 80'e çıkardık

                if (players != null && players.size() >= 2) {
                    Player p1 = players.get(0);
                    Player p2 = players.get(1);

                    // ========== PLAYER 1 (Sol) ==========
                    g.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
                    g.setColor(new Color(100, 200, 255)); // Açık mavi
                    g.drawString("PLAYER 1", 15, 20);

                    if (p1.isAlive()) {
                        // Ana istatistikler
                        g.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
                        g.setColor(Color.WHITE);
                        g.drawString("❤️ Health: " + p1.getHealth(), 15, 40);
                        g.drawString("💣 Bombs: " + p1.getCurrentBombs() + "/" + p1.getBombCount(), 15, 55);
                        g.drawString("💥 Power: " + p1.getBombPower(), 130, 55);
                        g.drawString("⚡ Speed: " + String.format("%.1f", p1.getSpeed()), 230, 55);

                        // Aktif efektler (yeni satırda)
                        drawActiveEffects(g, p1, 15, 70);

                    } else {
                        g.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
                        g.setColor(Color.RED);
                        g.drawString("☠️ ELIMINATED", 15, 50);
                    }

                    // ========== PLAYER 2 (Sağ) ==========
                    int p2X = getWidth() - 320; // Daha fazla boşluk

                    g.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
                    g.setColor(new Color(255, 150, 100)); // Açık turuncu
                    g.drawString("PLAYER 2", p2X, 20);

                    if (p2.isAlive()) {
                        // Ana istatistikler
                        g.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
                        g.setColor(Color.WHITE);
                        g.drawString("❤️ Health: " + p2.getHealth(), p2X, 40);
                        g.drawString("💣 Bombs: " + p2.getCurrentBombs() + "/" + p2.getBombCount(), p2X, 55);
                        g.drawString("💥 Power: " + p2.getBombPower(), p2X + 115, 55);
                        g.drawString("⚡ Speed: " + String.format("%.1f", p2.getSpeed()), p2X + 215, 55);

                        // Aktif efektler (yeni satırda)
                        drawActiveEffects(g, p2, p2X, 70);

                    } else {
                        g.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
                        g.setColor(Color.RED);
                        g.drawString("☠️ ELIMINATED", p2X, 50);
                    }

                    // Tema göstergesi (sağ alt köşe)
                    ThemeManager themeManager = ThemeManager.getInstance();
                    g.setColor(new Color(255, 255, 255, 150));
                    g.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
                    g.drawString(themeManager.getThemeName(), getWidth() - 150, getHeight() - 25);

                    // ========== ORTA BİLGİ (Kontroller) ==========
                    g.setColor(new Color(255, 255, 255, 120));
                    g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 10));
                    int centerX = getWidth() / 2 - 150;
                    g.drawString("P1: WASD + SPACE  |  P2: Arrows + ENTER  | 1/2/3: Theme |  P: Pause  |  ESC: Quit", centerX, getHeight() - 10);
                }
            }

// Aktif power-up efektlerini göster (güncellendi)
            private void drawActiveEffects(Graphics g, Player player, int x, int y) {
                g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 10));

                StringBuilder effects = new StringBuilder();

                // Shield
                if (player.isShieldActive()) {
                    effects.append("🛡️").append(String.format("%.0fs", player.getShieldTimer())).append(" ");
                }

                // Ghost
                if (player.isGhostMode()) {
                    effects.append("👻").append(String.format("%.0fs", player.getGhostTimer())).append(" ");
                }

                // Max Speed
                if (player.isMaxSpeedActive()) {
                    effects.append("🚀").append(String.format("%.0fs", player.getMaxSpeedTimer())).append(" ");
                }

                // Speed Boost
                if (player.getTempSpeedTimer() > 0) {
                    effects.append("⚡").append(String.format("%.0fs", player.getTempSpeedTimer())).append(" ");
                }

                // Bomb Power
                if (player.getTempBombPowerTimer() > 0) {
                    effects.append("💥").append(String.format("%.0fs", player.getTempBombPowerTimer())).append(" ");
                }

                // Bomb Count
                if (player.getTempBombCountTimer() > 0) {
                    effects.append("💣").append(String.format("%.0fs", player.getTempBombCountTimer())).append(" ");
                }

                // Aktif efekt varsa göster
                if (effects.length() > 0) {
                    g.setColor(Color.YELLOW);
                    g.drawString("Active: " + effects.toString(), x, y);
                }

            }


        private void drawExplosionCell(Graphics g, int x, int y) {
            int screenX = x * CELL_SIZE;
            int screenY = y * CELL_SIZE;

            // Turuncu patlama efekti
            g.setColor(new Color(255, 140, 0, 200));
            g.fillRect(screenX, screenY, CELL_SIZE, CELL_SIZE);

            // Sarı kenarlık
            g.setColor(new Color(255, 255, 0, 150));
            g.fillRect(screenX + 5, screenY + 5, CELL_SIZE - 10, CELL_SIZE - 10);

            // Kırmızı merkez
            g.setColor(new Color(255, 0, 0, 100));
            g.fillRect(screenX + 15, screenY + 15, CELL_SIZE - 30, CELL_SIZE - 30);
        }

        private void drawNotifications(Graphics g) {
            for (Notification notification : notifications) {
                int x = notification.getX();
                int y = notification.getY();
                String message = notification.getMessage();

                // Alpha (fade out)
                int alpha = (int) (notification.getAlpha() * 255);

                // Gölge
                g.setColor(new Color(0, 0, 0, alpha));
                g.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
                g.drawString(message, x + 2, y + 2);

                // Ana text
                g.setColor(new Color(255, 255, 255, alpha));
                g.drawString(message, x, y);
            }
        }
    }
}