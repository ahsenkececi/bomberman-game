package com.bomberman.state;

public class MenuState implements IGameState {

    @Override
    public void enter() {
        System.out.println("\n╔════════════════════════════╗");
        System.out.println("║     BOMBERMAN - MENU       ║");
        System.out.println("╚════════════════════════════╝");
        System.out.println("1. Start Game");
        System.out.println("2. Leaderboard");
        System.out.println("3. Settings");
        System.out.println("4. Exit");
    }

    @Override
    public void update() {
        // Menu animasyonları vs.
    }

    @Override
    public void exit() {
        System.out.println("Exiting menu...");
    }

    @Override
    public void handleInput(String input) {
        System.out.println("Menu received input: " + input);
    }
}