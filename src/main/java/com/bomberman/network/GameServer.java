package com.bomberman.network;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class GameServer {

    private ServerSocket serverSocket;
    private List<ClientHandler> clients;
    private boolean running;
    private static final int PORT = 8888;

    public GameServer() {
        clients = new CopyOnWriteArrayList<>();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            running = true;

            System.out.println("🌐 Server started on port " + PORT);
            System.out.println("Waiting for players...");

            // Client bağlantılarını dinle
            while (running && clients.size() < 2) {
                Socket clientSocket = serverSocket.accept();

                int playerId = clients.size() + 1;
                ClientHandler handler = new ClientHandler(clientSocket, playerId, this);
                clients.add(handler);

                new Thread(handler).start();

                System.out.println("✅ Player " + playerId + " connected. Total players: " + clients.size());

                // İki oyuncu bağlandı mı?
                if (clients.size() == 2) {
                    System.out.println("🎮 Both players connected! Starting game...");

                    // Tüm client'lara GAME_START gönder
                    NetworkMessage startMsg = new NetworkMessage(
                            NetworkMessage.MessageType.GAME_START,
                            "Game Starting!"
                    );
                    broadcastMessage(startMsg);
                }
            }

        } catch (IOException e) {
            System.err.println("❌ Server error: " + e.getMessage());
        }
    }

    public void stop() {
        running = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            for (ClientHandler client : clients) {
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Tüm clientlara mesaj gönder
    public void broadcastMessage(NetworkMessage message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    // Bir client hariç herkese gönder (gönderenin kendisine gönderme)
    public void broadcastMessage(NetworkMessage message, int senderPlayerId) {
        for (ClientHandler client : clients) {
            if (client.getPlayerId() != senderPlayerId) {
                client.sendMessage(message);
            }
        }
    }

    // Main metod - Server'ı başlat
    public static void main(String[] args) {
        GameServer server = new GameServer();
        server.start();
    }

    // İç sınıf - Her client için thread
    class ClientHandler implements Runnable {
        private Socket socket;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private int playerId;
        private GameServer server;

        public ClientHandler(Socket socket, int playerId, GameServer server) {
            this.socket = socket;
            this.playerId = playerId;
            this.server = server;

            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                // Player ID'yi gönder
                NetworkMessage welcomeMsg = new NetworkMessage(
                        NetworkMessage.MessageType.PLAYER_CONNECTED,
                        "You are Player " + playerId
                );
                welcomeMsg.setPlayerId(playerId);
                sendMessage(welcomeMsg);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    NetworkMessage message = (NetworkMessage) in.readObject();

                    System.out.println("📩 Received: " + message);

                    // Diğer client'a ilet
                    server.broadcastMessage(message, playerId);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Player " + playerId + " disconnected");
            } finally {
                close();
            }
        }

        public void sendMessage(NetworkMessage message) {
            try {
                out.writeObject(message);
                out.flush();
            } catch (IOException e) {
                System.err.println("Failed to send message to Player " + playerId);
            }
        }

        public int getPlayerId() {
            return playerId;
        }

        public void close() {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}