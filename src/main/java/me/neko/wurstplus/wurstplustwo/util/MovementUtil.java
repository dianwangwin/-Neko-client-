package me.neko.wurstplus.wurstplustwo.util;

import me.neko.wurstplus.NekoHax;

public class MovementUtil extends RuntimeException { //NoStackTrace when crashes
    public MovementUtil(String msg) {
        super(msg);
        setStackTrace(new StackTraceElement[0]);
    }

    @Override
    public String toString() {
        return "" + NekoHax.get_version();
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}

