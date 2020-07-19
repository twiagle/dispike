package com.twiagle.dispike.spike.redis;

public class UserKey extends BasePrefix {
    /**
     * always not expire
     * @param prefix
     */
    private UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");
}
