package com.bomberman.factory;

import com.bomberman.models.*;

public class WallFactory {

    // Factory Method
    public static Wall createWall(WallType type, int x, int y, String theme) {
        switch (type) {
            case UNBREAKABLE:
                return new UnbreakableWall(x, y, theme);

            case BREAKABLE:
                return new BreakableWall(x, y, theme);

            case HARD:
                return new HardWall(x, y, theme);

            default:
                throw new IllegalArgumentException("Invalid wall type: " + type);
        }
    }

    // Overload: Theme olmadan (default "Desert")
    public static Wall createWall(WallType type, int x, int y) {
        return createWall(type, x, y, "Desert");
    }
}