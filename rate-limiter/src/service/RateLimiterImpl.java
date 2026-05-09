package service;

import model.User;
import service.factory.RateLimiterFactory;
import service.strategy.RateLimiterStrategy;

public class RateLimiterImpl implements RateLimiter {

    @Override
    public boolean allowRequest(User user) {
        RateLimiterStrategy strategy = RateLimiterFactory.getRateLimiter(user.getUserTier());
        if (strategy == null) {
            throw new IllegalArgumentException("No rate limiter for tier: " + user.getUserTier());
        }
        return strategy.allowRequest(user);
    }
}
