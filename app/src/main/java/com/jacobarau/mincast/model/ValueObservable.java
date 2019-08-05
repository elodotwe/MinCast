package com.jacobarau.mincast.model;

import java.util.Observable;
import java.util.Observer;

public class ValueObservable<T> extends Observable {
    private T currentValue = null;

    public synchronized void onValueChanged(T newValue) {
        setChanged();
        notifyObservers(newValue);
        currentValue = newValue;
    }

    /**
     * Add the given observer and immediately notify all observers of the current value.
     *
     * Meant for use with UI data binding.
     * @param o The observer that will be watching this observable
     */
    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
        if (currentValue == null) {
            return;
        }
        onValueChanged(currentValue);
    }
}
