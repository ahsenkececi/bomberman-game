package com.bomberman.controllers;

import com.bomberman.database.DatabaseManager;
import com.bomberman.decorator.*;
import com.bomberman.factory.*;
import com.bomberman.models.*;
import com.bomberman.observer.GameEventManager;
import com.bomberman.state.*;
import com.bomberman.views.GameView;
import com.bomberman.views.GameWindow;
import com.bomberman.theme.ThemeManager;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    // MVC Components
    private Game gameModel;
    private GameView gameView;
    private GameStateManager stateManager;
    private InputController inputController;
    private GameWindow gameWindow;

    // ✅ YENİ: Network controller
    private NetworkController networkController;
    private boolean isOnlineMode;

    // Game objects
    private Map gameMap;

    // ✅ IPlayer kullan (Decorator için)
    private IPlayer player1;
    private IPlayer player2;

    // ✅ Gerçek Player referansları (Observer, isAlive, moveUp için)
    private Player realPlayer1;
    private Player realPlayer2;

    private List<Bomb> activeBombs;
    private List<Explosion> explosions;
    private List<Notification> notifications;

    // Singletons
    private DatabaseManager dbManager;
    private GameEventManager eventManager;

    // Game loop
    private boolean running;
    private final int FPS = 30;
    private final long FRAME_TIME = 1000 / FPS;

    public GameController() {
        // Model
        gameModel = new Game();

        // View
        gameView = new GameView();

        // Input
        inputController = new InputController();

        // State Manager
        stateManager = GameStateManager.getInstance();

        // Singletons
        dbManager = DatabaseManager.getInstance();
        eventManager = GameEventManager.getInstance();

        // ✅ YENİ: Network
        networkController = new NetworkController(this);
        isOnlineMode = false;

        // Lists
        activeBombs = new ArrayList<>();
        explosions = new ArrayList<>();
        notifications = new ArrayList<>();
    }

    // ✅ YENİ: Online modda başlat (Host)
    public void startAsHost() {
        System.out.println("🌐 Starting as HOST...");
        isOnlineMode = true;
        networkController.startAsHost();

        // Host initialization
        initializeGame();
        // Diğer oyuncu bağlanana kadar bekle
        System.out.println("⏳ Waiting for other player to connect...");

        // ✅ GEÇİCİ: 5 saniye bekle sonra başlat
        new Thread(() -> {
            try {
                Thread.sleep(5000);  // 5 saniye bekle
                System.out.println("🎮 Starting game after 5 seconds...");
                startGame();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // ✅ YENİ: Online modda başlat (Client)
    public void startAsClient(String serverIp) {
        System.out.println("🌐 Connecting to server: " + serverIp);
        isOnlineMode = true;

        boolean connected = networkController.connectToServer(serverIp, 8888);

        if (connected) {
            System.out.println("✅ Connected! Waiting for game to start...");
            initializeGame();

            // ✅ GEÇİCİ: 3 saniye bekle sonra başlat
            new Thread(() -> {
                try {
                    Thread.sleep(3000);  // 3 saniye bekle
                    System.out.println("🎮 Starting game after 3 seconds...");
                    startGame();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        } else {
            System.out.println("❌ Failed to connect to server");
            isOnlineMode = false;
        }
    }

    // Oyunu başlat
    public void initializeGame() {
        System.out.println("\n🎮 Initializing game...\n");
        System.out.println("Mode: " + (isOnlineMode ? "ONLINE" : "LOCAL"));

        // Harita oluştur (15x13)
        gameMap = new Map(15, 13);

        // Dış duvarlar (Unbreakable)
        for (int i = 0; i < 15; i++) {
            Wall top = WallFactory.createWall(WallType.UNBREAKABLE, i, 0);
            Wall bottom = WallFactory.createWall(WallType.UNBREAKABLE, i, 12);
            gameMap.addWall(top);
            gameMap.addWall(bottom);
        }
        for (int i = 1; i < 12; i++) {
            Wall left = WallFactory.createWall(WallType.UNBREAKABLE, 0, i);
            Wall right = WallFactory.createWall(WallType.UNBREAKABLE, 14, i);
            gameMap.addWall(left);
            gameMap.addWall(right);
        }

        // İç duvarlar (pattern)
        for (int i = 2; i < 13; i += 2) {
            for (int j = 2; j < 11; j += 2) {
                Wall wall = WallFactory.createWall(WallType.UNBREAKABLE, i, j);
                gameMap.addWall(wall);
            }
        }

        // Bazı breakable duvarlar ekle
        gameMap.addWall(WallFactory.createWall(WallType.BREAKABLE, 3, 3));
        gameMap.addWall(WallFactory.createWall(WallType.BREAKABLE, 5, 5));
        gameMap.addWall(WallFactory.createWall(WallType.BREAKABLE, 7, 7));
        gameMap.addWall(WallFactory.createWall(WallType.BREAKABLE, 9, 9));
        gameMap.addWall(WallFactory.createWall(WallType.HARD, 11, 5));

        System.out.println("✅ Map created (15x13)");

        // ✅ YENİ: Düşmanları ekle
        Enemy staticEnemy = EnemyFactory.createEnemy(EnemyType.STATIC, 7, 6);
        staticEnemy.setMoveDelay(999f);

        Enemy chasingEnemy = EnemyFactory.createEnemy(EnemyType.CHASING, 10, 8);
        chasingEnemy.setMoveDelay(0.8f);

        Enemy intelligentEnemy = EnemyFactory.createEnemy(EnemyType.INTELLIGENT, 4, 9);
        intelligentEnemy.setMoveDelay(1.2f);

        gameModel.addEnemy(staticEnemy);
        gameModel.addEnemy(chasingEnemy);
        gameModel.addEnemy(intelligentEnemy);

        System.out.println("✅ Enemies spawned:");
        System.out.println("  - Static enemy at (7, 6)");
        System.out.println("  - Chasing enemy at (10, 8)");
        System.out.println("  - Intelligent enemy at (4, 9)");

        // Power-up'lar oluştur
        gameModel.addPowerUp(PowerUpFactory.createPowerUp(PowerUpType.SPEED_BOOST, 3, 5));
        gameModel.addPowerUp(PowerUpFactory.createPowerUp(PowerUpType.BOMB_POWER, 11, 7));
        gameModel.addPowerUp(PowerUpFactory.createPowerUp(PowerUpType.BOMB_COUNT, 7, 9));
        System.out.println("✅ Power-ups spawned on map");

        // Oyuncular oluştur
        realPlayer1 = new Player("Player1", 1, 1);
        realPlayer2 = new Player("Player2", 13, 11);

        // IPlayer olarak başlat (Decorator için)
        player1 = realPlayer1;
        player2 = realPlayer2;

        // Observer olarak kaydet
        eventManager.attach(realPlayer1);
        eventManager.attach(realPlayer2);

        gameModel.addPlayer(realPlayer1);
        gameModel.addPlayer(realPlayer2);

        System.out.println("✅ Players created");
        System.out.println("  " + player1);
        System.out.println("  " + player2);

        System.out.println("\n✅ Game initialization complete!\n");
        System.out.println("Controls:");
        System.out.println("  Player 1: WASD to move, SPACE to place bomb");
        System.out.println("  Player 2: Arrow Keys to move, ENTER to place bomb");
        System.out.println("  Press P to pause, ESC to quit");
        System.out.println("  Press 1/2/3 to change theme\n");

        // Swing window oluştur
        gameWindow = new GameWindow(inputController);
        System.out.println("✅ Swing window created\n");
    }

    // Oyun döngüsü
    public void startGame() {
        gameView.displayWelcome();

        // State: Menu -> Playing
        stateManager.changeState(new PlayingState());
        gameModel.start();
        running = true;

        System.out.println("\n🎮 Game started! Press any movement key...\n");

        // Game loop
        long lastTime = System.currentTimeMillis();
        int turnCount = 0;

        while (running) {
            long currentTime = System.currentTimeMillis();
            long deltaTime = currentTime - lastTime;

            if (deltaTime >= FRAME_TIME) {
                turnCount++;

                // Input işle
                handleInput();

                // Oyunu güncelle
                update(deltaTime / 1000.0f);

                // Render (Swing)
                if (gameWindow != null) {
                    gameWindow.render(gameMap, gameModel.getPlayers(), activeBombs,
                            gameModel.getEnemies(), explosions, notifications,
                            gameModel.getPowerUps());
                }

                // Oyun bitti mi?
                if (checkGameOver()) {
                    running = false;
                }

                lastTime = currentTime;
            }

            // CPU'yu yorma
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Oyun sonu
        endGame();
    }

    // Input işle
    private void handleInput() {
        // ✅ ONLINE MOD: Sadece kendi player'ını kontrol et
        if (isOnlineMode) {
            int myPlayerId = networkController.getMyPlayerId();
            Player myPlayer = (myPlayerId == 1) ? realPlayer1 : realPlayer2;

            if (!myPlayer.isAlive()) return;

            // Hareket kontrolü
            boolean moved = false;
            int oldX = myPlayer.getX();
            int oldY = myPlayer.getY();

            if (inputController.isP1Up()) {
                moved = myPlayer.moveUp(gameMap);
            } else if (inputController.isP1Down()) {
                moved = myPlayer.moveDown(gameMap);
            } else if (inputController.isP1Left()) {
                moved = myPlayer.moveLeft(gameMap);
            } else if (inputController.isP1Right()) {
                moved = myPlayer.moveRight(gameMap);
            }

            // Hareket ettiyse network'e bildir
            if (moved) {
                networkController.sendPlayerMove(myPlayer.getX(), myPlayer.getY());
            }

            // Bomba
            if (inputController.isP1Bomb()) {
                if (myPlayer.canPlaceBomb()) {
                    IPlayer decoratedPlayer = (myPlayerId == 1) ? player1 : player2;
                    int decoratedPower = decoratedPlayer.getBombPower();

                    Bomb bomb = new Bomb(myPlayer.getX(), myPlayer.getY(), decoratedPower);
                    activeBombs.add(bomb);
                    myPlayer.bombExploded();

                    // Network'e bildir
                    networkController.sendBombPlaced(myPlayer.getX(), myPlayer.getY());

                    inputController.clear();
                }
            }

        } else {
            // ✅ LOCAL MOD: Her iki player'ı kontrol et
            handleLocalInput();
        }
        // Pause
        if (inputController.isPause()) {
            System.out.println("⏸️  Game paused");
            stateManager.changeState(new PausedState());
            inputController.clear();
        }

        // Quit
        if (inputController.isEscape()) {
            System.out.println("👋 Quitting game...");
            running = false;
        }

        // Tema değiştirme
        if (inputController.isThemeDesert()) {
            ThemeManager.getInstance().setTheme(Theme.DESERT);
            inputController.clear();
        }

        if (inputController.isThemeForest()) {
            ThemeManager.getInstance().setTheme(Theme.FOREST);
            inputController.clear();
        }

        if (inputController.isThemeCity()) {
            ThemeManager.getInstance().setTheme(Theme.CITY);
            inputController.clear();
        }
    }

    // ✅ YENİ: Local input handler
    private void handleLocalInput() {
        // Player 1 hareketi
        if (realPlayer1.isAlive()) {
            if (inputController.isP1Up()) {
                realPlayer1.moveUp(gameMap);
            } else if (inputController.isP1Down()) {
                realPlayer1.moveDown(gameMap);
            } else if (inputController.isP1Left()) {
                realPlayer1.moveLeft(gameMap);
            } else if (inputController.isP1Right()) {
                realPlayer1.moveRight(gameMap);
            }

            if (inputController.isP1Bomb()) {
                if (realPlayer1.canPlaceBomb()) {
                    int decoratedPower = player1.getBombPower();
                    Bomb bomb = new Bomb(realPlayer1.getX(), realPlayer1.getY(), decoratedPower);
                    activeBombs.add(bomb);
                    realPlayer1.bombExploded();
                    inputController.clear();
                }
            }
        }

        // Player 2 hareketi
        if (realPlayer2.isAlive()) {
            if (inputController.isP2Up()) {
                realPlayer2.moveUp(gameMap);
            } else if (inputController.isP2Down()) {
                realPlayer2.moveDown(gameMap);
            } else if (inputController.isP2Left()) {
                realPlayer2.moveLeft(gameMap);
            } else if (inputController.isP2Right()) {
                realPlayer2.moveRight(gameMap);
            }

            if (inputController.isP2Bomb()) {
                if (realPlayer2.canPlaceBomb()) {
                    int decoratedPower = player2.getBombPower();
                    Bomb bomb = new Bomb(realPlayer2.getX(), realPlayer2.getY(), decoratedPower);
                    activeBombs.add(bomb);
                    realPlayer2.bombExploded();
                    inputController.clear();
                }
            }
        }
    }

    // Oyunu güncelle
    private void update(float deltaTime) {
        // Bombaları güncelle
        List<Bomb> explodedBombs = new ArrayList<>();

        for (Bomb bomb : activeBombs) {
            bomb.update(deltaTime);

            if (bomb.isExploded()) {
                explodedBombs.add(bomb);
                explosions.add(new Explosion(bomb.getX(), bomb.getY(), bomb.getPower()));
                handleExplosion(bomb);

                for (Player player : gameModel.getPlayers()) {
                    player.bombExploded();
                }
            }
        }

        // ✅ YENİ: Düşmanları güncelle
        for (Enemy enemy : gameModel.getEnemies()) {
            if (enemy.isAlive()) {
                // Strategy pattern ile hareket et
                // Hedef: En yakın player
                Player targetPlayer = getClosestPlayer(enemy);
                if (targetPlayer != null) {
                    enemy.update(deltaTime, gameMap, targetPlayer);
                }
            }
        }

        // Power-up'ları güncelle
        for (PowerUp powerUp : gameModel.getPowerUps()) {
            powerUp.update(deltaTime);
        }

        // Patlayan bombaları kaldır
        activeBombs.removeAll(explodedBombs);

        // Explosion'ları güncelle
        List<Explosion> finishedExplosions = new ArrayList<>();
        for (Explosion explosion : explosions) {
            explosion.update(deltaTime);
            if (explosion.isFinished()) {
                finishedExplosions.add(explosion);
            }
        }
        explosions.removeAll(finishedExplosions);

        // Notification'ları güncelle
        List<Notification> finishedNotifications = new ArrayList<>();
        for (Notification notification : notifications) {
            notification.update(deltaTime);
            if (notification.isFinished()) {
                finishedNotifications.add(notification);
            }
        }
        notifications.removeAll(finishedNotifications);

        // Power-up toplama kontrolü
        checkPowerUpCollection();
        checkEnemyCollisions();
    }

    // En yakın player'ı bul
    private Player getClosestPlayer(Enemy enemy) {
        Player closest = null;
        int minDistance = Integer.MAX_VALUE;

        for (Player player : gameModel.getPlayers()) {
            if (!player.isAlive()) continue;

            int distance = Math.abs(player.getX() - enemy.getX()) +
                    Math.abs(player.getY() - enemy.getY());

            if (distance < minDistance) {
                minDistance = distance;
                closest = player;
            }
        }

        return closest;
    }
    // Enemy çarpışma kontrolü
    private void checkEnemyCollisions() {
        for (Enemy enemy : gameModel.getEnemies()) {
            if (!enemy.isAlive()) continue;

            for (Player player : gameModel.getPlayers()) {
                if (!player.isAlive()) continue;

                // Aynı pozisyonda mı?
                if (enemy.getX() == player.getX() && enemy.getY() == player.getY()) {
                    System.out.println("👾 " + player.getName() + " touched an enemy!");
                    player.takeDamage();
                    addNotification("👾 ENEMY!", player.getX(), player.getY());
                }
            }
        }
    }

    // Power-up toplama kontrolü
    private void checkPowerUpCollection() {
        List<PowerUp> collectedPowerUps = new ArrayList<>();

        for (PowerUp powerUp : gameModel.getPowerUps()) {
            // realPlayer ile kontrol et
            if (realPlayer1.isAlive() &&
                    realPlayer1.getX() == powerUp.getX() &&
                    realPlayer1.getY() == powerUp.getY()) {

                collectPowerUp(realPlayer1, powerUp);
                collectedPowerUps.add(powerUp);

            } else if (realPlayer2.isAlive() &&
                    realPlayer2.getX() == powerUp.getX() &&
                    realPlayer2.getY() == powerUp.getY()) {

                collectPowerUp(realPlayer2, powerUp);
                collectedPowerUps.add(powerUp);
            }
        }

        for (PowerUp powerUp : collectedPowerUps) {
            gameModel.getPowerUps().remove(powerUp);
        }
    }

    // ✅ Power-up topla (DECORATOR PATTERN)
    private void collectPowerUp(Player player, PowerUp powerUp) {
        String powerUpName = powerUp.getName();
        int px = player.getX();
        int py = player.getY();

        System.out.println("\n🎁 " + player.getName() + " collected " + powerUpName + "!");

        // ✅ DECORATOR PATTERN ile sarmal
        if (player == realPlayer1) {
            player1 = applyDecorator(player1, powerUp);
            addNotification(getNotificationText(powerUp), px, py);
            System.out.println("  Player1 Decorated Stats: " + player1);

        } else if (player == realPlayer2) {
            player2 = applyDecorator(player2, powerUp);
            addNotification(getNotificationText(powerUp), px, py);
            System.out.println("  Player2 Decorated Stats: " + player2);
        }
    }

    // ✅ Decorator uygula
    private IPlayer applyDecorator(IPlayer player, PowerUp powerUp) {
        if (powerUp instanceof SpeedBoostPowerUp) {
            return new SpeedBoostDecorator(player);

        } else if (powerUp instanceof BombPowerPowerUp) {
            return new BombPowerDecorator(player);

        } else if (powerUp instanceof BombCountPowerUp) {
            return new BombCountDecorator(player);
        }

        return player;
    }

    // ✅ Notification text helper
    private String getNotificationText(PowerUp powerUp) {
        if (powerUp instanceof SpeedBoostPowerUp) {
            return "⚡ SPEED UP!";
        } else if (powerUp instanceof BombPowerPowerUp) {
            return "💥 POWER UP!";
        } else if (powerUp instanceof BombCountPowerUp) {
            return "💣 +1 BOMB!";
        }
        return "🎁 Power-up!";
    }

    // Patlama hasarını hesapla
    private void handleExplosion(Bomb bomb) {
        int bx = bomb.getX();
        int by = bomb.getY();
        int power = bomb.getPower();

        System.out.println("\n💥 BOMB EXPLODED at (" + bx + "," + by + ")");
        addNotification("💥 BOOM!", bx, by);

        // Patlama alanındaki oyuncuları kontrol et
        for (Player player : gameModel.getPlayers()) {
            if (!player.isAlive()) continue;

            int px = player.getX();
            int py = player.getY();
            int distance = Math.abs(px - bx) + Math.abs(py - by);

            boolean sameRow = (py == by);
            boolean sameCol = (px == bx);

            if ((sameRow || sameCol) && distance <= power) {
                int oldHealth = player.getHealth();
                player.takeDamage();

                if (player.isAlive()) {
                    System.out.println("  ⚠️  " + player.getName() + " hit! Health: " +
                            oldHealth + " → " + player.getHealth());
                    addNotification("❤️ -1 HP", px, py);
                } else {
                    System.out.println("  ☠️  " + player.getName() + " died!");
                    addNotification("☠️ DEAD!", px, py);
                }
            }
        }
        // ✅ YENİ: Patlama alanındaki düşmanları kontrol et
        for (Enemy enemy : gameModel.getEnemies()) {
            if (!enemy.isAlive()) continue;

            int ex = enemy.getX();
            int ey = enemy.getY();
            int distance = Math.abs(ex - bx) + Math.abs(ey - by);

            boolean sameRow = (ey == by);
            boolean sameCol = (ex == bx);

            if ((sameRow || sameCol) && distance <= power) {
                enemy.die();
                System.out.println("  👾 Enemy killed at (" + ex + "," + ey + ")");
                addNotification("💀 ENEMY KILLED!", ex, ey);

                // Observer pattern - enemy öldü eventi
                eventManager.notifyObservers("ENEMY_DIED", enemy);
            }
        }

        // Duvarları yok et
        destroyWalls(bx, by, power);
    }

    // Duvarları yok et
    private void destroyWalls(int bx, int by, int power) {
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

        for (int[] dir : directions) {
            for (int i = 1; i <= power; i++) {
                int x = bx + (dir[0] * i);
                int y = by + (dir[1] * i);

                if (!gameMap.isInBounds(x, y)) break;

                int cell = gameMap.getCell(x, y);

                if (cell == 1) {
                    break; // Unbreakable
                } else if (cell == 2) {
                    gameMap.removeWall(x, y);
                    System.out.println("  🧱 Breakable wall destroyed at (" + x + "," + y + ")");
                    addNotification("💨 Destroyed", x, y);

                    // %30 şans power-up spawn
                    if (Math.random() < 0.3) {
                        PowerUp randomPowerUp = PowerUpFactory.createRandomPowerUp(x, y);
                        gameModel.addPowerUp(randomPowerUp);
                        System.out.println("  🎁 Power-up spawned: " + randomPowerUp.getName());
                    }

                    break;
                } else if (cell == 3) {
                    // Hard wall - TODO
                    break;
                }
            }
        }
    }

    // Notification ekle
    private void addNotification(String message, int gridX, int gridY) {
        int screenX = gridX * 50 + 25;
        int screenY = gridY * 50 + 25;
        notifications.add(new Notification(message, screenX, screenY));
    }

    // Oyun durumunu göster
    private void displayGameState() {
        System.out.println("\n" + realPlayer1);
        System.out.println("  Decorated: " + player1);
        System.out.println(realPlayer2);
        System.out.println("  Decorated: " + player2);
        System.out.println("Active bombs: " + activeBombs.size());
    }

    // Oyun bitti mi?
    private boolean checkGameOver() {
        // ✅ realPlayer kullan (IPlayer'da isAlive() yok)
        boolean p1Alive = realPlayer1.isAlive();
        boolean p2Alive = realPlayer2.isAlive();

        if (!p1Alive && !p2Alive) {
            return true;
        }

        if (!p1Alive || !p2Alive) {
            return true;
        }

        return false;
    }

    // Oyun sonu
    private void endGame() {
        String winner;

        // ✅ realPlayer kullan
        if (realPlayer1.isAlive() && !realPlayer2.isAlive()) {
            winner = "Player 1";
        } else if (realPlayer2.isAlive() && !realPlayer1.isAlive()) {
            winner = "Player 2";
        } else {
            winner = "Draw";
        }

        stateManager.changeState(new GameOverState(winner));
        gameView.displayGameOver(winner);
        gameModel.stop();
    }

    // Ana kontrol metodu (LOCAL mode için)
    public void run() {
        if (!isOnlineMode) {
            initializeGame();
        }
        startGame();
        cleanup();
    }

    // Temizlik
    private void cleanup() {
        System.out.println("\n🧹 Cleaning up...");

        // ✅ Network bağlantısını kapat
        if (isOnlineMode && networkController != null) {
            networkController.disconnect();
        }

        dbManager.closeConnection();
        System.out.println("✅ Cleanup complete");
    }

    // Input controller'ı dışarı ver (UI için)
    public InputController getInputController() {
        return inputController;
    }
    // =====================================
    // NETWORK CALLBACK METODLARI
    // =====================================

    // Oyun başladığında
    public void onNetworkGameStart() {
        System.out.println("🎮 Network game starting!");
        // Oyun zaten initialize edildi, sadece start et
        if (!running) {
            new Thread(() -> {
                startGame();
            }).start();
        }
    }

    // Diğer oyuncu hareket ettiğinde
    public void onNetworkPlayerMove(int playerId, int x, int y) {
        System.out.println("📍 Player " + playerId + " moved to (" + x + "," + y + ")");

        // Diğer player'ın pozisyonunu güncelle
        Player otherPlayer = (playerId == 1) ? realPlayer1 : realPlayer2;

        if (otherPlayer != null) {
            otherPlayer.move(x, y);
        }
    }

    // Diğer oyuncu bomba koyduğunda
    public void onNetworkBombPlaced(int playerId, int x, int y) {
        System.out.println("💣 Player " + playerId + " placed bomb at (" + x + "," + y + ")");

        Player player = (playerId == 1) ? realPlayer1 : realPlayer2;
        IPlayer decoratedPlayer = (playerId == 1) ? player1 : player2;

        if (player != null) {
            int power = decoratedPlayer.getBombPower();
            Bomb bomb = new Bomb(x, y, power);
            activeBombs.add(bomb);
            player.bombExploded();
        }
    }

    // Bomba patladığında (senkronizasyon için)
    public void onNetworkBombExploded(int x, int y, int power) {
        System.out.println("💥 Network bomb exploded at (" + x + "," + y + ")");
        // Patlama zaten local'de de gerçekleşiyor
        // Bu sadece senkronizasyon için
    }

    // Diğer oyuncu power-up topladığında
    public void onNetworkPowerUpCollected(int playerId, int x, int y, String powerUpType) {
        System.out.println("🎁 Player " + playerId + " collected " + powerUpType + " at (" + x + "," + y + ")");

        // Power-up'ı haritadan kaldır
        gameModel.getPowerUps().removeIf(p -> p.getX() == x && p.getY() == y);

        // Decorator uygula
        if (playerId == 1) {
            player1 = applyDecoratorByName(player1, powerUpType);
            System.out.println("  Player1 Decorated: " + player1);
        } else {
            player2 = applyDecoratorByName(player2, powerUpType);
            System.out.println("  Player2 Decorated: " + player2);
        }
    }

    // Oyuncu öldüğünde
    public void onNetworkPlayerDied(int playerId) {
        System.out.println("☠️  Player " + playerId + " died!");

        Player player = (playerId == 1) ? realPlayer1 : realPlayer2;
        if (player != null) {
            // Player zaten local'de de ölüyor
            // Bu sadece senkronizasyon
        }
    }

    // Oyun bittiğinde
    public void onNetworkGameOver(String winner) {
        System.out.println("🏁 Game Over! Winner: " + winner);
        running = false;
    }

    // ✅ YENİ: Power-up türüne göre decorator uygula
    private IPlayer applyDecoratorByName(IPlayer player, String powerUpType) {
        switch (powerUpType) {
            case "Speed Boost":
                return new SpeedBoostDecorator(player);
            case "Bomb Power":
                return new BombPowerDecorator(player);
            case "Bomb Count":
                return new BombCountDecorator(player);
            default:
                return player;
        }
    }

    // ✅ YENİ: Network controller'ı dışarı ver
    public NetworkController getNetworkController() {
        return networkController;
    }

    // ✅ YENİ: Online mod kontrolü
    public boolean isOnlineMode() {
        return isOnlineMode;
    }
}
