package com.bomberman.state;

public class PlayingState implements IGameState {

    @Override
    public void enter() {
        System.out.println("\n╔════════════════════════════╗");
        System.out.println("║      GAME STARTED!         ║");
        System.out.println("╚════════════════════════════╝");
        System.out.println("Players spawned...");
        System.out.println("Map loaded...");
        System.out.println("Enemies spawned...");
        System.out.println("🎮 Game is running!");
    }

    @Override
    public void update() {
        // Game loop - player movement, enemy AI, bomb timers, etc.
        System.out.println("  [Game loop running...]");
    }

    @Override
    public void exit() {
        System.out.println("Pausing game state...");
    }

    @Override
    public void handleInput(String input) {
        if (input.equals("PAUSE")) {
            System.out.println("Game paused!");
        } else if (input.equals("PLACE_BOMB")) {
            System.out.println("💣 Bomb placed!");
        } else {
            System.out.println("Player action: " + input);
        }
    }
}