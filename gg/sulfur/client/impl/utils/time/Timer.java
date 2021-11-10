package gg.sulfur.client.impl.utils.time;

public class Timer {

    private long prevMS;

    public boolean hasPassed(double milliSec) {
        return (float) (this.getTime() - this.prevMS) >= milliSec;
    }

    public void reset() {
        this.prevMS = this.getTime();
    }

    public long getTime() {
        return System.nanoTime() / 1000000;
    }

    public boolean sleep(long time) {
        if (getTime() >= time) {
            reset();
            return true;
        }
        return false;
    }


}