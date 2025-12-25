package com.bomberman.network;

import java.io.*;
import java.net.*;

public class GameClient {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private int myPlayerId;
    private MessageListener listener;
    private boolean connected;

    public interface MessageListener {
        void onMessageReceived(NetworkMessage message);
    }

    public GameClient(MessageListener listener) {
        this.listener = listener;
        this.connected = false;
    }

    public boolean connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            connected = true;
            System.out.println("🌐 Connected to server: " + host + ":" + port);

            // Mesaj dinleme thread'i başlat
            new Thread(this::receiveMessages).start();

            return true;

        } catch (IOException e) {
            System.err.println("❌ Connection failed: " + e.getMessage());
            return false;
        }
    }

    private void receiveMessages() {
        try {
            while (connected) {
                NetworkMessage message = (NetworkMessage) in.readObject();

                System.out.println("📩 Received: " + message);

                // İlk mesaj - Player ID
                if (message.getType() == NetworkMessage.MessageType.PLAYER_CONNECTED) {
                    myPlayerId = message.getPlayerId();
                    System.out.println("✅ You are Player " + myPlayerId);
                }

                // Listener'a bildir
                if (listener != null) {
                    listener.onMessageReceived(message);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Connection lost");
            connected = false;
        }
    }

    public void sendMessage(NetworkMessage message) {
        if (!connected) {
            System.err.println("Not connected to server!");
            return;
        }

        try {
            message.setPlayerId(myPlayerId);
            out.writeObject(message);
            out.flush();
            System.out.println("📤 Sent: " + message);
        } catch (IOException e) {
            System.err.println("Failed to send message");
            e.printStackTrace();
        }
    }

    public void disconnect() {
        connected = false;
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getMyPlayerId() {
        return myPlayerId;
    }

    public boolean isConnected() {
        return connected;
    }
}