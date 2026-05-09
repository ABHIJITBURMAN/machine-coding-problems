package service.strategy;

import model.RateLimiterConfig;
import model.User;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class TokenBucketRateLimiter implements RateLimiterStrategy {
    private final int capacity;
    private final double refillRate;

    private final ConcurrentHashMap<String, BucketState> states = new ConcurrentHashMap<>();

    private static final class BucketState {
        double tokens;
        long lastRefillTime;
    }

    public TokenBucketRateLimiter(RateLimiterConfig config) {
        this.capacity = config.getCapacity();
        this.refillRate = config.getRefillRate();
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity must be non-negative");
        }
        if (refillRate < 0) {
            throw new IllegalArgumentException("refillRate must be non-negative");
        }
    }

    @Override
    public boolean allowRequest(User user) {
        String userId = user.getUserId();
        long now = user.getTimestamp().longValue();

        AtomicBoolean allowed = new AtomicBoolean(false);
        states.compute(userId, (id, state) -> {
            BucketState b = state != null ? state : newBucket(now);
            long elapsed = now - b.lastRefillTime;
            if (elapsed > 0 && refillRate > 0) {
                b.tokens = Math.min(capacity, b.tokens + elapsed * refillRate);
                b.lastRefillTime = now;
            }
            if (b.tokens >= 1.0 - 1e-9) {
                b.tokens -= 1.0;
                allowed.set(true);
            }
            return b;
        });
        return allowed.get();
    }

    private BucketState newBucket(long now) {
        BucketState b = new BucketState();
        b.tokens = capacity;
        b.lastRefillTime = now;
        return b;
    }
}
