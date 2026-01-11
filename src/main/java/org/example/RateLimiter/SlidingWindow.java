package org.example.RateLimiter;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SlidingWindow implements RateLimiter {
    Queue<Integer> slidingWindow;
    int bucketCap;
    int time;

    public SlidingWindow(int time, int cap){
        this.time = time;
        this.bucketCap = cap;
        slidingWindow = new ConcurrentLinkedQueue<>();
    }

    @Override
    public boolean grantAccess() {
        long currentTime = System.currentTimeMillis();
        updateQueue(currentTime);
        if(slidingWindow.size() < bucketCap){
            slidingWindow.offer((int) currentTime);
            return true;
        }
        return false;
    }

    private void updateQueue(long currentTime){
        if(slidingWindow.isEmpty()) return;
        long time = (currentTime - slidingWindow.peek()) / 1000;

        while (time >= this.time){
            slidingWindow.poll();
            if(slidingWindow.isEmpty()) break;
            time = (currentTime - slidingWindow.peek()) / 1000;
        }
    }
}


