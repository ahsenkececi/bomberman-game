package com.bomberman.factory;

import com.bomberman.models.*;
import com.bomberman.strategy.*;

public class EnemyFactory {

    // Factory Method - Strategy Pattern ile birlikte
    public static Enemy createEnemy(EnemyType type, int x, int y) {
        IEnemyBehavior behavior;

        switch (type) {
            case STATIC:
                behavior=new StaticBehavior();
                break;

            case CHASING:
                behavior = new ChasingBehavior();
                break;

            case INTELLIGENT:
                behavior= new IntelligentBehavior();
                break;

            default:
                throw new IllegalArgumentException("Invalid enemy type: " + type);
        }

        return new Enemy(x,y,behavior);
    }
}