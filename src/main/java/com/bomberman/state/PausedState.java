package com.bomberman.state;

public class PausedState implements IGameState {

    @Override
    public void enter() {
        System.out.println("\n╔════════════════════════════╗");
        System.out.println("║       GAME PAUSED          ║");
        System.out.println("╚════════════════════════════╝");
        System.out.println("1. Resume");
        System.out.println("2. Settings");
        System.out.println("3. Quit to Menu");
    }

    @Override
    public void update() {
        // Pause menü animasyonları
    }

    @Override
    public void exit() {
        System.out.println("Resuming game...");
    }

    @Override
    public void handleInput(String input) {
        System.out.println("Pause menu received input: " + input);
    }
}