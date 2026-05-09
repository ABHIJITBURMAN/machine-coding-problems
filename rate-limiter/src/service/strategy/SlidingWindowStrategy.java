package service.strategy;

import model.RateLimiterConfig;
import model.User;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class SlidingWindowStrategy implements RateLimiterStrategy {
    private final int windowSize;
    private final int maxRequests;

    /** Per user: last timestamp and count (same semantics as prior implementation; updated atomically per key). */
    private final ConcurrentHashMap<String, SlidingState> states = new ConcurrentHashMap<>();

    private static final class SlidingState {
        int lastTimestamp;
        int requestCount;
    }

    public SlidingWindowStrategy(RateLimiterConfig config) {
        this.maxRequests = config.getMaxRequests();
        this.windowSize = config.getWindowDuration();
        if (windowSize <= 0) {
            throw new IllegalArgumentException("windowDuration must be positive");
        }
    }

    @Override
    public boolean allowRequest(User user) {
        String userId = user.getUserId();
        int currentTimestamp = user.getTimestamp();

        AtomicBoolean allowed = new AtomicBoolean(false);
        states.compute(userId, (id, state) -> {
            SlidingState s = state != null ? state : new SlidingState();
            int lastTimeStamp = s.lastTimestamp;
            int requestCount = s.requestCount;
            int delta = currentTimestamp - lastTimeStamp;
            if (delta > windowSize) {
                s.lastTimestamp = currentTimestamp;
                s.requestCount = 1;
                allowed.set(true);
            } else if (requestCount < maxRequests) {
                s.lastTimestamp = currentTimestamp;
                s.requestCount = requestCount + 1;
                allowed.set(true);
            }
            return s;
        });
        return allowed.get();
    }
}
