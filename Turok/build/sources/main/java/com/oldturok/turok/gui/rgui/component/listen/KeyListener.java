package com.oldturok.turok.gui.rgui.component.listen;

public interface KeyListener {

    public void onKeyDown(KeyEvent event);
    public void onKeyUp(KeyEvent event);

    public static class KeyEvent {
        int key;

        public KeyEvent(int key) {
            this.key = key;
        }

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }
    }
}
