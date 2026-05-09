package service.factory;

import model.RateLimiterConfig;
import model.UserTier;
import service.strategy.FixedWindowStrategy;
import service.strategy.RateLimiterStrategy;
import service.strategy.SlidingWindowStrategy;
import service.strategy.TokenBucketRateLimiter;

import java.util.Map;

public class RateLimiterFactory {

    private static final Map<UserTier, RateLimiterStrategy> strategyMap = Map.of(
            UserTier.FREE, new FixedWindowStrategy(new RateLimiterConfig(2,5,0,0.0)),
            UserTier.PREMIUM, new SlidingWindowStrategy(new RateLimiterConfig(2,2,0,0.0)),
            UserTier.ENTERPRISE, new TokenBucketRateLimiter(new RateLimiterConfig(0,0,1,0.25))

    );

    public static RateLimiterStrategy getRateLimiter(UserTier userTier) {
        return strategyMap.get(userTier);
    }
}
