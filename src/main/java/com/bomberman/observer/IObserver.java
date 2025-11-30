package com.bomberman.observer;

public interface IObserver {
    // Olay olduğunda bildirim al
    void onNotify(String eventType, Object data);
}