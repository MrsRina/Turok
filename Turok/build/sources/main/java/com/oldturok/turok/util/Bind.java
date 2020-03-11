package com.oldturok.turok.util;

import org.lwjgl.input.Keyboard;

// Update by Rina 05/03/20.
public class Bind {
    int key;

    public Bind(boolean ignore_, boolean ignore__, boolean ignore___, int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public boolean isEmpty() {
        return key < 0;
    }

    public void setKey(int key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return isEmpty() ? "None" : (key < 0 ? "None" : capitalise(Keyboard.getKeyName(key)));
    }

    public boolean isDown(int eventKey) {
        return !isEmpty() && eventKey == getKey();
    }

    public String capitalise(String str) {
        if (str.isEmpty()) return "";
        return Character.toUpperCase(str.charAt(0)) + (str.length() != 1 ? str.substring(1).toLowerCase() : "");
    }

    public static Bind none() {
        return new Bind(false, false, false, -1);
    }
}