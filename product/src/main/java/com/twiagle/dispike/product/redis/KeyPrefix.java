package com.twiagle.dispike.product.redis;

public interface KeyPrefix {
    public int expireSeconds();
    public String getPrefix();
}
