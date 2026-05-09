package model;

public class User {
    private String userId;
    private UserTier userTier;

    private Integer timestamp;

    public User(String userId, UserTier userTier, Integer timestamp) {
        this.userId = userId;
        this.userTier = userTier;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }
    public UserTier getUserTier() {
        return userTier;
    }

    public Integer getTimestamp() {
        return timestamp;
    }
}
