package com.bomberman;

import com.bomberman.views.MainWindow;
import javax.swing.SwingUtilities;

import com.bomberman.controllers.GameController;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Swing GUI'yi başlat
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
        Scanner scanner = new Scanner(System.in);

        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║                                       ║");
        System.out.println("║        💣 BOMBERMAN GAME 💣          ║");
        System.out.println("║                                       ║");
        System.out.println("╚═══════════════════════════════════════╝\n");

        System.out.println("Select Mode:");
        System.out.println("1. Local Multiplayer (Hot-Seat)");
        System.out.println("2. Online Multiplayer (Host)");
        System.out.println("3. Online Multiplayer (Join)");
        System.out.print("\nYour choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        GameController controller = new GameController();

        switch (choice) {
            case 1:
                System.out.println("\n🎮 Starting LOCAL game...\n");
                controller.run();
                break;

            case 2:
                System.out.println("\n🌐 Starting as HOST...\n");
                controller.startAsHost();

                // Diğer oyuncu bağlanana kadar bekle
                System.out.println("⏳ Waiting for other player...");
                System.out.println("Tell them to connect to your IP!");

                // Game start mesajı gelince otomatik başlayacak
                break;

            case 3:
                System.out.print("\n🌐 Enter server IP (or 'localhost'): ");
                String serverIp = scanner.nextLine();

                System.out.println("Connecting to " + serverIp + "...\n");
                controller.startAsClient(serverIp);
                break;

            default:
                System.out.println("Invalid choice!");
                return;
        }

        scanner.close();
    }
}