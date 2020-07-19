package com.twiagle.dispike.customer.redis;

public class SpikePrefix extends BasePrefix {

    private SpikePrefix(String prefix) {
        super(prefix);
    }

    public static SpikePrefix isGoodsSoldOut = new SpikePrefix("gso");//for MQReceiver and SpikeService
}
