package com.twiagle.dispike.customer.redis;

public class OrderPrefix extends BasePrefix {

    public OrderPrefix(int expiredSeconds, String prefix) {
        super(expiredSeconds, prefix);
    }

    public static OrderPrefix getSpikeOrderByUidGid = new OrderPrefix(3600, "soug");
}
