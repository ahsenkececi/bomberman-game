package com.bomberman.network;

public class TestClient {
    public static void main(String[] args) {
        GameClient client = new GameClient(message -> {
            System.out.println("Received message: " + message);
        });

        boolean connected = client.connect("localhost", 8888);

        if (connected) {
            System.out.println("Connected! Waiting for other player...");

            // Test mesajı gönder
            try {
                Thread.sleep(2000);
                NetworkMessage testMsg = new NetworkMessage(
                        NetworkMessage.MessageType.PLAYER_MOVE,
                        client.getMyPlayerId(),
                        5,
                        5
                );
                client.sendMessage(testMsg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}