package service.strategy;

import model.RateLimiterConfig;
import model.User;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class FixedWindowStrategy implements RateLimiterStrategy {

    private final int maxRequests;
    private final long windowSizeInSeconds;

    /** Per user: fixed-window index and request count in that window (updated atomically per key). */
    private final ConcurrentHashMap<String, WindowState> states = new ConcurrentHashMap<>();

    private static final class WindowState {
        long windowIndex;
        int count;
    }

    public FixedWindowStrategy(RateLimiterConfig config) {
        this.maxRequests = config.getMaxRequests();
        this.windowSizeInSeconds = config.getWindowDuration();
        if (windowSizeInSeconds <= 0) {
            throw new IllegalArgumentException("windowDuration must be positive");
        }
    }

    @Override
    public boolean allowRequest(User user) {
        String userId = user.getUserId();
        long currentTime = user.getTimestamp().longValue();
        long currentWindow = currentTime / windowSizeInSeconds;

        AtomicBoolean allowed = new AtomicBoolean(false);
        states.compute(userId, (id, state) -> {
            WindowState s = state != null ? state : new WindowState();
            if (s.windowIndex < currentWindow) {
                s.windowIndex = currentWindow;
                s.count = 1;
                allowed.set(true);
            } else if (s.count < maxRequests) {
                s.count++;
                allowed.set(true);
            }
            return s;
        });
        return allowed.get();
    }
}
