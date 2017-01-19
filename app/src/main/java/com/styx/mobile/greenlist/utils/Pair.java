package com.styx.mobile.greenlist.utils;

/**
 * An implementation of a simple Key Value Pair
 */

public class Pair<T> {
    private T key, value;

    public Pair(T key, T value) {
        this.key = key;
        this.value = value;
    }

    public T getKey() {
        return key;
    }

    public void setKey(T key) {
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
