package com.bomberman.factory;

import com.bomberman.models.*;

public class EnemyFactory {

    // Factory Method
    public static Enemy createEnemy(EnemyType type, int x, int y) {
        switch (type) {
            case STATIC:
                return new Enemy(x, y, "Static");

            case CHASING:
                return new Enemy(x, y, "Chasing");

            case INTELLIGENT:
                return new Enemy(x, y, "Intelligent");

            default:
                throw new IllegalArgumentException("Invalid enemy type: " + type);
        }
    }
}