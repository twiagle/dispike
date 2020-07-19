package com.twiagle.dispike.order.redis;

public interface KeyPrefix {
    public int expireSeconds();
    public String getPrefix();
}
