package org.example.RateLimiter;

public class FixedWindow implements RateLimiter{

    private final int windowSizeInSeconds;
    private final int maxRequests;

    private long windowStart;
    private int counter;

    public FixedWindow(int windowSizeInSeconds, int maxRequests) {
        this.windowSizeInSeconds = windowSizeInSeconds;
        this.maxRequests = maxRequests;
        this.windowStart = System.currentTimeMillis();
        this.counter = 0;
    }

    @Override
    public synchronized boolean grantAccess() {
        long now = System.currentTimeMillis();

        if((now - windowStart)/1000 >= windowSizeInSeconds){
            windowStart = now;
            counter = 0;
        }

        if(counter < maxRequests) {
            counter++;
            return true;
        }
        return false;
    }
}
