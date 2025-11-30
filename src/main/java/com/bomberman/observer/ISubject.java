package com.bomberman.observer;

public interface ISubject {
    // Observer ekle
    void attach(IObserver observer);

    // Observer çıkar
    void detach(IObserver observer);

    // Tüm observer'lara bildir
    void notifyObservers(String eventType, Object data);
}