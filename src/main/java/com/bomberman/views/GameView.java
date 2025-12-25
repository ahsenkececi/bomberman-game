package com.bomberman.views;

import com.bomberman.models.*;
import java.util.List;

public class GameView {

    // Oyun başlangıç ekranı
    public void displayWelcome() {
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.println("║                                       ║");
        System.out.println("║        💣 BOMBERMAN GAME 💣          ║");
        System.out.println("║                                       ║");
        System.out.println("║    Design Patterns Implementation     ║");
        System.out.println("║                                       ║");
        System.out.println("╚═══════════════════════════════════════╝\n");
    }

    // Tur bilgisi
    public void displayTurn(int turnNumber) {
        System.out.println("\n════════════ TURN " + turnNumber + " ════════════");
    }

    // Oyuncu durumları
    public void displayPlayers(List<Player> players) {
        System.out.println("\n👥 PLAYERS:");
        for (Player player : players) {
            System.out.println("  " + player);
        }
    }

    // Düşman durumları
    public void displayEnemies(List<Enemy> enemies) {
        System.out.println("\n👾 ENEMIES:");
        for (Enemy enemy : enemies) {
            System.out.println("  " + enemy);
        }
    }

    // Bombalar
    public void displayBombs(List<Bomb> bombs) {
        if (!bombs.isEmpty()) {
            System.out.println("\n💣 BOMBS:");
            for (Bomb bomb : bombs) {
                System.out.println("  " + bomb);
            }
        }
    }

    // Duvarlar
    public void displayWalls(List<Wall> walls) {
        System.out.println("\n🧱 WALLS: " + walls.size() + " walls on map");
    }

    // Power-up'lar
    public void displayPowerUps(List<PowerUp> powerUps) {
        if (!powerUps.isEmpty()) {
            System.out.println("\n🎁 POWER-UPS:");
            for (PowerUp powerUp : powerUps) {
                System.out.println("  " + powerUp);
            }
        }
    }

    // Oyun sonu
    public void displayGameOver(String winner) {
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.println("║          GAME OVER!                   ║");
        System.out.println("║                                       ║");
        System.out.println("║  🏆 Winner: " + String.format("%-23s", winner) + "║");
        System.out.println("║                                       ║");
        System.out.println("╚═══════════════════════════════════════╝");
    }

    // Genel oyun durumu
    public void displayGameState(Game game) {
        displayPlayers(game.getPlayers());
        displayEnemies(game.getEnemies());
        displayBombs(game.getBombs());
        displayWalls(game.getWalls());
        displayPowerUps(game.getPowerUps());
    }

    // Basit harita (ASCII)
    public void displayMap(int width, int height, List<Wall> walls, List<Player> players, List<Enemy> enemies) {
        System.out.println("\n🗺️  MAP (" + width + "x" + height + "):");

        char[][] map = new char[height][width];

        // Boşlukları doldur
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map[i][j] = '.';
            }
        }

        // Duvarları ekle
        for (Wall wall : walls) {
            if (wall.getX() < width && wall.getY() < height) {
                map[wall.getY()][wall.getX()] = wall.getSymbol().charAt(0);
            }
        }

        // Oyuncuları ekle
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            if (p.getX() < width && p.getY() < height) {
                map[p.getY()][p.getX()] = (char) ('P' + i); // P1, P2, etc.
            }
        }

        // Düşmanları ekle
        for (Enemy enemy : enemies) {
            if (enemy.getX() < width && enemy.getY() < height && enemy.isAlive()) {
                map[enemy.getY()][enemy.getX()] = 'E';
            }
        }

        // Haritayı çiz
        for (int i = 0; i < height; i++) {
            System.out.print("  ");
            for (int j = 0; j < width; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("\n  Legend: . = Empty  # = Unbreakable  B = Breakable  H = Hard");
        System.out.println("          P = Player  E = Enemy");
    }
}