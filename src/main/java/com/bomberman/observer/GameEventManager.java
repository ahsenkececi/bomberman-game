package com.bomberman.observer;

import java.util.ArrayList;
import java.util.List;

public class GameEventManager implements ISubject {

    private static GameEventManager instance;
    private List<IObserver> observers;

    private GameEventManager() {
        observers = new ArrayList<>();
    }

    // Singleton
    public static GameEventManager getInstance() {
        if (instance == null) {
            instance = new GameEventManager();
        }
        return instance;
    }

    @Override
    public void attach(IObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("✅ Observer attached. Total: " + observers.size());
        }
    }

    @Override
    public void detach(IObserver observer) {
        observers.remove(observer);
        System.out.println("❌ Observer detached. Total: " + observers.size());
    }

    @Override
    public void notifyObservers(String eventType, Object data) {
        System.out.println("📢 Event: " + eventType + " - Notifying " + observers.size() + " observers...");
        for (IObserver observer : observers) {
            observer.onNotify(eventType, data); //TUM OBSERBER'LARA BİLDİR
        }
    }
}