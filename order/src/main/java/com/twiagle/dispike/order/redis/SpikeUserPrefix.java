package com.twiagle.dispike.order.redis;

public class SpikeUserPrefix extends BasePrefix {

    public static final int TOKEN_EXPIRE = 3600 * 24 * 2;
    public static SpikeUserPrefix token = new SpikeUserPrefix(TOKEN_EXPIRE, "token");
    public static SpikeUserPrefix getById = new SpikeUserPrefix(0, "id");

    private SpikeUserPrefix(int expiredSeconds, String prefix) {
        super(expiredSeconds,prefix);
    }

}
