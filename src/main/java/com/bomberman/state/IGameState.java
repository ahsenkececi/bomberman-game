package com.bomberman.state;

public interface IGameState {
    void enter();
    void update();
    void exit();
    void handleInput(String input);
}