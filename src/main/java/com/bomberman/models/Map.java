package com.bomberman.models;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private int width;
    private int height;
    private int[][] grid; // 0=empty, 1=unbreakable, 2=breakable, 3=hard
    private List<Wall> walls;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new int[height][width];
        this.walls = new ArrayList<>();

        // Grid'i boş yap
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = 0;
            }
        }
    }

    // Duvar ekle
    public void addWall(Wall wall) {
        walls.add(wall);

        int x = wall.getX();
        int y = wall.getY();

        if (isInBounds(x, y)) {
            if (wall instanceof UnbreakableWall) {
                grid[y][x] = 1;
            } else if (wall instanceof BreakableWall) {
                grid[y][x] = 2;
            } else if (wall instanceof HardWall) {
                grid[y][x] = 3;
            }
        }
    }

    // Duvar sil
    public void removeWall(int x, int y) {
        if (isInBounds(x, y)) {
            grid[y][x] = 0;
            walls.removeIf(wall -> wall.getX() == x && wall.getY() == y);
        }
    }

    // Pozisyon yürünebilir mi?
    public boolean isWalkable(int x, int y) {
        // Sınırlar içinde mi?
        if (!isInBounds(x, y)) {
            return false;
        }

        // Duvar var mı?
        return grid[y][x] == 0;
    }

    // Sınırlar içinde mi?
    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    // Grid değerini al
    public int getCell(int x, int y) {
        if (isInBounds(x, y)) {
            return grid[y][x];
        }
        return -1; // Out of bounds
    }

    // Getters
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Wall> getWalls() {
        return walls;
    }

    // Haritayı konsola yazdır (debug için)
    public void printMap() {
        System.out.println("\n🗺️  MAP:");
        for (int i = 0; i < height; i++) {
            System.out.print("  ");
            for (int j = 0; j < width; j++) {
                switch (grid[i][j]) {
                    case 0: System.out.print(". "); break;
                    case 1: System.out.print("# "); break;
                    case 2: System.out.print("B "); break;
                    case 3: System.out.print("H "); break;
                    default: System.out.print("? "); break;
                }
            }
            System.out.println();
        }
    }
}