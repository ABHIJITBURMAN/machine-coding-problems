package service;

import model.User;

public interface RateLimiter {
    boolean allowRequest(User user);
}
