package com.brandtnewtonsoftware.asle.util;

public class Stopwatch {

    private long startedAt;
    private long previousTime;

    public Stopwatch() {
        startedAt = 0;
        previousTime = 0;
    }

    public void start() {
        if (!isRunning()) {
            startedAt = System.currentTimeMillis();
        }
    }

    public void reset() {
        startedAt = 0;
        previousTime = 0;
    }

    public long stop() {
        previousTime = getTimeSinceStarted();
        startedAt = 0;
        return previousTime;
    }

    private long getTimeSinceStarted() {
        return System.currentTimeMillis() - startedAt;
    }

    public boolean isRunning() {
        return startedAt != 0;
    }

    public long getTimeElapsed() {
        if (isRunning()) {
            return getTimeSinceStarted();
        } else {
            return previousTime;
        }
    }
}
