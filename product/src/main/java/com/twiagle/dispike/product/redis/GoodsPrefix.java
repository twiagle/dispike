package com.twiagle.dispike.product.redis;

public class GoodsPrefix extends BasePrefix {

    public static GoodsPrefix getGoodsList = new GoodsPrefix(60, "gl");
    public static GoodsPrefix getGoodsDetail = new GoodsPrefix(60, "gd");
    public static GoodsPrefix getSpikeGoodsStock = new GoodsPrefix(0,"gs");

    public GoodsPrefix(int expiredSeconds, String prefix) {
        super(expiredSeconds, prefix);
    }
}
