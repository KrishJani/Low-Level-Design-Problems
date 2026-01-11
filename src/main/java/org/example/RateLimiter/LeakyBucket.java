package org.example.RateLimiter;

public class LeakyBucket implements RateLimiter {
    private final int capacity;
    private final int leakRatePerSecond;

    private int water;
    private long lastLeakTimestamp;

    public LeakyBucket(int capacity, int leakRatePerSecond) {
        this.capacity = capacity;
        this.leakRatePerSecond = leakRatePerSecond;
        this.water = 0;
        this.lastLeakTimestamp = System.currentTimeMillis();
    }

    @Override
    public synchronized boolean grantAccess() {
        leak();

        if (water < capacity) {
            water++;
            return true;
        }
        return false;
    }

    private void leak() {
        long now = System.currentTimeMillis();
        long secondsPassed = (now - lastLeakTimestamp)/1000;

        int leaked = (int) (secondsPassed * leakRatePerSecond);
        if(leaked > 0) {
            water = Math.max(0, water - leaked);
            lastLeakTimestamp = now;
        }
    }
}
