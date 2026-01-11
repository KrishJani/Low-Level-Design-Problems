package org.example.RateLimiter;

public class SlidingWindowCounter implements RateLimiter {
    private final int windowSizeInSeconds;
    private final int maxRequests;

    private long currentWindowStart;
    private int currentCount;
    private int previousCount;

    public SlidingWindowCounter(int windowSizeInSeconds, int maxRequests) {
        this.windowSizeInSeconds = windowSizeInSeconds;
        this.maxRequests = maxRequests;
        this.currentWindowStart = System.currentTimeMillis();
    }

    @Override
    public synchronized boolean grantAccess() {
        long now = System.currentTimeMillis();
        long elapsedSeconds = (now - currentWindowStart) / 1000;

        if(elapsedSeconds >= windowSizeInSeconds) {
            previousCount = currentCount;
            currentCount = 0;
            currentWindowStart = now;
            elapsedSeconds = 0;
        }

        double weight = (double) (windowSizeInSeconds - elapsedSeconds) / windowSizeInSeconds;
        double estimatedCount = previousCount*weight + currentCount;

        if(estimatedCount < maxRequests) {
            currentCount++;
            return true;
        }

        return false;
    }
}
