package com.jacobarau.mincast.model;

import java.util.Observable;
import java.util.Observer;

public class ValueObservable<T> extends Observable {
    private T currentValue = null;
    private final Object observerLock = new Object();

    public void onValueChanged(T newValue) {
        synchronized (observerLock) {
            setChanged();
            notifyObservers(newValue);
            currentValue = newValue;
        }
    }

    /**
     * Add the given observer and immediately notify all observers of the current value.
     *
     * Meant for use with UI data binding.
     *
     * Note that the Observer may be called after {@link ValueObservable#deleteObserver(Observer)}
     * is called--it is the responsibility of the Observer to not crash if it does receive such
     * spurious calls.
     *
     * @param o The observer that will be watching this observable
     */
    @Override
    public void addObserver(Observer o) {
        synchronized (observerLock) {
            super.addObserver(o);
            if (currentValue == null) {
                return;
            }
            onValueChanged(currentValue);
        }
    }

    @Override
    public void deleteObserver(Observer o) {
        synchronized (observerLock) {
            super.deleteObserver(o);
        }
    }
}
