package com.twiagle.dispike.spike.redis;

/**
 * each child class only need to put its internal namespace, this class auto add their class name as outer namespace
 */

public abstract class BasePrefix implements KeyPrefix {
    private int expiredSeconds;
    private String prefix;

    @Override
    public int expireSeconds() {
        return expiredSeconds;
    }

    @Override
    public String getPrefix() {
        return this.getClass().getSimpleName()+":"+prefix;
    }

    //construction, never expire, sons will
    public BasePrefix(String prefix) {
        this(0, prefix);
    }

    public BasePrefix(int expiredSeconds, String prefix) {
        this.expiredSeconds = expiredSeconds;
        this.prefix = prefix;
    }
}
