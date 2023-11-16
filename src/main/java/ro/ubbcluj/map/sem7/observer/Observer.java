package ro.ubbcluj.map.sem7.observer;


import ro.ubbcluj.map.sem7.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}