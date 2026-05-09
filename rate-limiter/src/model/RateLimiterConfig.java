package model;

public final class RateLimiterConfig {
     private int maxRequests;
     private int windowDuration;
     private int capacity;
     private double refillRate;

    public RateLimiterConfig(int maxRequests, int windowDuration, int capacity, double refillRate) {
         this.maxRequests = maxRequests;
         this.windowDuration = windowDuration;
         this.capacity = capacity;
         this.refillRate = refillRate;
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public int getWindowDuration() {
        return windowDuration;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getRefillRate() {
        return refillRate;
    }
}
