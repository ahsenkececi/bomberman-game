package com.bomberman.state;

public class GameStateManager {
    private static GameStateManager instance;
    private IGameState currentState;

    private GameStateManager() {
        // Başlangıç state'i Menu
        currentState = new MenuState();
        currentState.enter();
    }

    // Singleton
    public static GameStateManager getInstance() {
        if (instance == null) {
            instance = new GameStateManager();
        }
        return instance;
    }

    // State değiştir
    public void changeState(IGameState newState) {
        System.out.println("\n🔄 State Transition...");
        currentState.exit();
        currentState = newState;
        currentState.enter();
    }

    // Güncel state'i güncelle
    public void update() {
        currentState.update();
    }

    // Input'u güncel state'e ilet
    public void handleInput(String input) {
        currentState.handleInput(input);
    }

    public IGameState getCurrentState() {
        return currentState;
    }
}