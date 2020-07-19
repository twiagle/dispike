package com.twiagle.dispike.spike.redis;

public interface KeyPrefix {
    public int expireSeconds();
    public String getPrefix();
}
