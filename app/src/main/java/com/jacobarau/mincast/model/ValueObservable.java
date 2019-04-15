package com.jacobarau.mincast.model;

import java.util.Observable;

public class ValueObservable<T> extends Observable {
    public void onValueChanged(T newValue) {
        setChanged();
        notifyObservers(newValue);
    }
}
