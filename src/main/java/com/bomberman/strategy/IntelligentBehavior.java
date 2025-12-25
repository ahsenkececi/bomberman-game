package com.bomberman.strategy;

import com.bomberman.models.Enemy;
import com.bomberman.models.Player;

import java.util.*;

public class IntelligentBehavior implements IEnemyBehavior {

    @Override
    public void move(Enemy enemy, com.bomberman.models.Map map, Player targetPlayer) {
        int enemyX = enemy.getX();
        int enemyY = enemy.getY();
        int playerX = targetPlayer.getX();
        int playerY = targetPlayer.getY();

        // A* ile en kısa yolu bul
        List<Node> path = findPath(map, enemyX, enemyY, playerX, playerY);

        if (path != null && path.size() > 1) {
            // İlk adım mevcut pozisyon, ikinci adım gidilecek yer
            Node nextStep = path.get(1);
            enemy.move(nextStep.x, nextStep.y);
            System.out.println("🧠 Intelligent enemy used A* pathfinding: (" +
                    enemyX + "," + enemyY + ") -> (" + nextStep.x + "," + nextStep.y + ")");
        } else {
            // Yol bulunamadı, hareketsiz kal
            System.out.println("🧠 Intelligent enemy: No path found");
        }
    }

    // A* Pathfinding Algoritması
    private List<Node> findPath(com.bomberman.models.Map map, int startX, int startY, int goalX, int goalY) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.fScore));
        Set<String> closedSet = new HashSet<>();
        java.util.Map<String, Node> allNodes = new HashMap<>();

        Node startNode = new Node(startX, startY, null);
        startNode.gScore = 0;
        startNode.fScore = heuristic(startX, startY, goalX, goalY);

        openSet.add(startNode);
        allNodes.put(startNode.getKey(), startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            // Hedefe ulaştık mı?
            if (current.x == goalX && current.y == goalY) {
                return reconstructPath(current);
            }

            closedSet.add(current.getKey());

            // Komşuları kontrol et (4 yön: yukarı, aşağı, sol, sağ)
            int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

            for (int[] dir : directions) {
                int newX = current.x + dir[0];
                int newY = current.y + dir[1];

                // Sınır kontrolü
                if (newX < 0 || newY < 0 || newX >= map.getWidth() || newY >= map.getHeight()) {
                    continue;
                }

                // Duvar kontrolü
                if (map.getCell(newX, newY) != 0) {
                    continue;  // Duvar var, geçilemez
                }

                String neighborKey = newX + "," + newY;

                // Zaten kapalı sette mi?
                if (closedSet.contains(neighborKey)) {
                    continue;
                }

                int tentativeGScore = current.gScore + 1;

                Node neighbor = allNodes.get(neighborKey);
                if (neighbor == null) {
                    neighbor = new Node(newX, newY, current);
                    allNodes.put(neighborKey, neighbor);
                }

                // Daha iyi bir yol bulundu mu?
                if (tentativeGScore < neighbor.gScore) {
                    neighbor.parent = current;
                    neighbor.gScore = tentativeGScore;
                    neighbor.fScore = neighbor.gScore + heuristic(newX, newY, goalX, goalY);

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return null;  // Yol bulunamadı
    }

    // Heuristic (Manhattan Distance)
    private int heuristic(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    // Yolu yeniden oluştur
    private List<Node> reconstructPath(Node goalNode) {
        List<Node> path = new ArrayList<>();
        Node current = goalNode;

        while (current != null) {
            path.add(0, current);  // Başa ekle
            current = current.parent;
        }

        return path;
    }

    @Override
    public String getBehaviorName() {
        return "Intelligent (A*)";
    }

    // İç sınıf - A* için Node
    private static class Node {
        int x, y;
        Node parent;
        int gScore = Integer.MAX_VALUE;  // Başlangıçtan buraya maliyet
        int fScore = Integer.MAX_VALUE;  // gScore + heuristic

        Node(int x, int y, Node parent) {
            this.x = x;
            this.y = y;
            this.parent = parent;
        }

        String getKey() {
            return x + "," + y;
        }
    }
}