package com.bomberman.controllers;

import com.bomberman.network.GameClient;
import com.bomberman.network.NetworkMessage;
import com.bomberman.network.NetworkMessage.MessageType;

public class NetworkController implements GameClient.MessageListener {

    private GameClient client;
    private GameController gameController;
    private boolean isHost;
    private int myPlayerId;

    public NetworkController(GameController gameController) {
        this.gameController = gameController;
    }

    // Server'a bağlan
    public boolean connectToServer(String host, int port) {
        client = new GameClient(this);
        boolean connected = client.connect(host, port);

        if (connected) {
            myPlayerId = client.getMyPlayerId();
            System.out.println("✅ Connected as Player " + myPlayerId);
        }

        return connected;
    }

    // Server olarak başlat (host)
    public void startAsHost() {
        isHost = true;
        // Server başlatma ayrı bir thread'de olmalı
        new Thread(() -> {
            com.bomberman.network.GameServer server = new com.bomberman.network.GameServer();
            server.start();
        }).start();

        // Kısa bir süre bekle server başlasın
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Kendi server'ına bağlan
        connectToServer("localhost", 8888);
    }

    // Mesaj gönder
    public void sendPlayerMove(int x, int y) {
        if (client != null && client.isConnected()) {
            NetworkMessage msg = new NetworkMessage(MessageType.PLAYER_MOVE, myPlayerId, x, y);
            client.sendMessage(msg);
        }
    }

    public void sendBombPlaced(int x, int y) {
        if (client != null && client.isConnected()) {
            NetworkMessage msg = new NetworkMessage(MessageType.BOMB_PLACED, myPlayerId, x, y);
            client.sendMessage(msg);
        }
    }

    public void sendBombExploded(int x, int y, int power) {
        if (client != null && client.isConnected()) {
            NetworkMessage msg = new NetworkMessage(MessageType.BOMB_EXPLODED, myPlayerId, x, y);
            msg.setData(String.valueOf(power));
            client.sendMessage(msg);
        }
    }

    public void sendPowerUpCollected(int x, int y, String powerUpType) {
        if (client != null && client.isConnected()) {
            NetworkMessage msg = new NetworkMessage(MessageType.POWERUP_COLLECTED, myPlayerId, x, y);
            msg.setData(powerUpType);
            client.sendMessage(msg);
        }
    }

    // Mesaj alındığında (callback)
    @Override
    public void onMessageReceived(NetworkMessage message) {
        System.out.println("🔔 NetworkController received: " + message);

        switch (message.getType()) {
            case GAME_START:
                System.out.println("🎮 Game starting!");
                gameController.onNetworkGameStart();
                break;

            case PLAYER_CONNECTED:
                // İlk mesaj - Player ID ataması
                myPlayerId = message.getPlayerId();
                System.out.println("✅ You are Player " + myPlayerId);
                break;

            case PLAYER_MOVE:
                gameController.onNetworkPlayerMove(message.getPlayerId(), message.getX(), message.getY());
                break;

            case BOMB_PLACED:
                gameController.onNetworkBombPlaced(message.getPlayerId(), message.getX(), message.getY());
                break;

            case BOMB_EXPLODED:
                int power = Integer.parseInt(message.getData());
                gameController.onNetworkBombExploded(message.getX(), message.getY(), power);
                break;

            case POWERUP_COLLECTED:
                gameController.onNetworkPowerUpCollected(message.getPlayerId(), message.getX(), message.getY(), message.getData());
                break;

            case PLAYER_DIED:
                gameController.onNetworkPlayerDied(message.getPlayerId());
                break;

            case GAME_OVER:
                gameController.onNetworkGameOver(message.getData());
                break;
        }
    }

    public int getMyPlayerId() {
        return myPlayerId;
    }

    public boolean isHost() {
        return isHost;
    }

    public void disconnect() {
        if (client != null) {
            client.disconnect();
        }
    }
}