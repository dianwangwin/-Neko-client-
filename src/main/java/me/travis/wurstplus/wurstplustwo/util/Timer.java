package me.travis.wurstplus.wurstplustwo.util;

public final class Timer {
    private long time = -1L;

    public boolean passed(double d) {
        return (double)(System.currentTimeMillis() - this.time) >= d;
    }

    public void reset() {
        this.time = System.currentTimeMillis();
    }

    public void resetTimeSkipTo(long l) {
        this.time = System.currentTimeMillis() + l;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long l) {
        this.time = l;
    }
}
