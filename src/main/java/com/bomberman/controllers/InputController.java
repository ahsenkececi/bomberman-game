package com.bomberman.controllers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class InputController implements KeyListener {

    // Tema değiştirme
    public static final int THEME_DESERT = KeyEvent.VK_1;
    public static final int THEME_FOREST = KeyEvent.VK_2;
    public static final int THEME_CITY = KeyEvent.VK_3;

    // Basılı tutulan tuşlar
    private Set<Integer> pressedKeys;

    // Player 1 kontrolleri (WASD + Space)
    public static final int P1_UP = KeyEvent.VK_W;
    public static final int P1_DOWN = KeyEvent.VK_S;
    public static final int P1_LEFT = KeyEvent.VK_A;
    public static final int P1_RIGHT = KeyEvent.VK_D;
    public static final int P1_BOMB = KeyEvent.VK_SPACE;

    // Player 2 kontrolleri (Arrow Keys + Enter)
    public static final int P2_UP = KeyEvent.VK_UP;
    public static final int P2_DOWN = KeyEvent.VK_DOWN;
    public static final int P2_LEFT = KeyEvent.VK_LEFT;
    public static final int P2_RIGHT = KeyEvent.VK_RIGHT;
    public static final int P2_BOMB = KeyEvent.VK_ENTER;

    // Oyun kontrolleri
    public static final int PAUSE = KeyEvent.VK_P;
    public static final int ESCAPE = KeyEvent.VK_ESCAPE;

    public InputController() {
        pressedKeys = new HashSet<>();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Kullanılmıyor
    }

    // Tuş basılı mı kontrolu
    public boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    // Player 1 kontrolleri
    public boolean isP1Up() {
        return isKeyPressed(P1_UP);
    }

    public boolean isP1Down() {
        return isKeyPressed(P1_DOWN);
    }

    public boolean isP1Left() {
        return isKeyPressed(P1_LEFT);
    }

    public boolean isP1Right() {
        return isKeyPressed(P1_RIGHT);
    }

    public boolean isP1Bomb() {
        return isKeyPressed(P1_BOMB);
    }

    // Player 2 kontrolleri
    public boolean isP2Up() {
        return isKeyPressed(P2_UP);
    }

    public boolean isP2Down() {
        return isKeyPressed(P2_DOWN);
    }

    public boolean isP2Left() {
        return isKeyPressed(P2_LEFT);
    }

    public boolean isP2Right() {
        return isKeyPressed(P2_RIGHT);
    }

    public boolean isP2Bomb() {
        return isKeyPressed(P2_BOMB);
    }

    // Oyun kontrolleri
    public boolean isPause() {
        return isKeyPressed(PAUSE);
    }

    public boolean isEscape() {
        return isKeyPressed(ESCAPE);
    }

    // Tüm tuşları temizle
    public void clear() {
        pressedKeys.clear();
    }
    public boolean isThemeDesert() {
        return isKeyPressed(THEME_DESERT);
    }

    public boolean isThemeForest() {
        return isKeyPressed(THEME_FOREST);
    }

    public boolean isThemeCity() {
        return isKeyPressed(THEME_CITY);
    }
}