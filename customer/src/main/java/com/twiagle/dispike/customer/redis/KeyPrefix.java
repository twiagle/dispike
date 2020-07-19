package com.twiagle.dispike.customer.redis;

public interface KeyPrefix {
    public int expireSeconds();
    public String getPrefix();
}
