package ro.ubbcluj.map.sem7.observer;


import ro.ubbcluj.map.sem7.events.Event;

public interface Observable<E extends Event> {
    void addObserver(Observer<E>  e);
    void removeObserver(Observer<E> e);
    void notifyObservers(E t);
}
