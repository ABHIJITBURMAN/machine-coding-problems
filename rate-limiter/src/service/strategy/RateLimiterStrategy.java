package service.strategy;

import model.User;

public interface RateLimiterStrategy {
    boolean allowRequest(User user);
}
