package org.example.RateLimiter;

public class TokenBucket implements RateLimiter{
    private final int capacity;
    private final int refillRatePerSecond;

    private double tokens;
    private long lastRefillTimestamp;

    public TokenBucket(int capacity, int refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRatePerSecond = refillRatePerSecond;
        this.tokens = capacity;
        this.lastRefillTimestamp = System.currentTimeMillis();
    }

    @Override
    public synchronized boolean grantAccess() {
        refill();

        if(tokens >= 1) {
            tokens--;
            return true;
        }
        return false;
    }

    public void refill() {
        long now = System.currentTimeMillis();
        double secondsPassed = (now - lastRefillTimestamp)/1000.0;
        double tokensToAdd = secondsPassed * refillRatePerSecond;
        tokens = Math.min(capacity, tokens+tokensToAdd);
        lastRefillTimestamp = now;
    }
}
