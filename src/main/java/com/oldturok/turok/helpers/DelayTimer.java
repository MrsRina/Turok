package com.oldturok.turok.helpers;

public class DelayTimer {
    public long time;

    public DelayTimer() {
        this.time = (System.nanoTime() / 1000000L);
    }

    public boolean hasTimeElapsed(long time, boolean reset) {
        boolean elapsed = getTime() >= time;
        if (elapsed && reset) {
            resetTime();
        }
        return elapsed;
    }

    public long getTime() {
        return System.nanoTime() / 1000000L - this.time;
    }

    public void resetTime() {
        this.time = (System.nanoTime() / 1000000L);
    }
}
