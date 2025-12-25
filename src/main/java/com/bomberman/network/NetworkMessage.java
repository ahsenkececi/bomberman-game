package com.bomberman.network;

import java.io.Serializable;

public class NetworkMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private MessageType type;
    private int playerId;
    private int x, y;
    private String data;

    public enum MessageType {
        PLAYER_CONNECTED,
        PLAYER_MOVE,
        BOMB_PLACED,
        BOMB_EXPLODED,
        PLAYER_DIED,
        GAME_START,
        GAME_OVER,
        POWERUP_COLLECTED,
        SYNC_STATE
    }

    // Constructors
    public NetworkMessage(MessageType type) {
        this.type = type;
    }

    public NetworkMessage(MessageType type, int playerId, int x, int y) {
        this.type = type;
        this.playerId = playerId;
        this.x = x;
        this.y = y;
    }

    public NetworkMessage(MessageType type, String data) {
        this.type = type;
        this.data = data;
    }

    // Getters/Setters
    public MessageType getType() {
        return type;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "NetworkMessage[" + type + ", Player:" + playerId + ", Pos:(" + x + "," + y + ")]";
    }
}