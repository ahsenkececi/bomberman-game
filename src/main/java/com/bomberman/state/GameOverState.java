package com.bomberman.state;

public class GameOverState implements IGameState {
    private String winner;

    public GameOverState(String winner) {
        this.winner = winner;
    }

    @Override
    public void enter() {
        System.out.println("\n╔════════════════════════════╗");
        System.out.println("║       GAME OVER!           ║");
        System.out.println("╚════════════════════════════╝");
        System.out.println("🏆 Winner: " + winner);
        System.out.println("\n1. Rematch");
        System.out.println("2. Return to Menu");
        System.out.println("3. View Stats");
    }

    @Override
    public void update() {
        // Game over animasyonları
    }

    @Override
    public void exit() {
        System.out.println("Leaving game over screen...");
    }

    @Override
    public void handleInput(String input) {
        System.out.println("Game over screen received input: " + input);
    }
}