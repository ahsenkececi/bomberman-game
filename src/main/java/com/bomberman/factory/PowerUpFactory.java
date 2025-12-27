package com.bomberman.factory;

import com.bomberman.models.*;

import java.util.Random;

public class PowerUpFactory {

    private static Random random = new Random();

    // Factory Method
    public static PowerUp createPowerUp(PowerUpType type, int x, int y) {
        switch (type) {
            case SPEED_BOOST:
                return new SpeedBoostPowerUp(x, y);

            case BOMB_POWER:
                return new BombPowerPowerUp(x, y);

            case BOMB_COUNT:
                return new BombCountPowerUp(x, y);

            case SHIELD:  // ✅ YENİ!
                return new ShieldPowerUp(x, y);

            case GHOST:  // ✅ YENİ!
                return new GhostPowerUp(x, y);

            default:
                throw new IllegalArgumentException("Invalid power-up type: " + type);
        }
    }

    // Random power-up üret
    public static PowerUp createRandomPowerUp(int x, int y) {
        PowerUpType[] types = PowerUpType.values();
        PowerUpType randomType = types[random.nextInt(types.length)];
        return createPowerUp(randomType, x, y);
    }
}