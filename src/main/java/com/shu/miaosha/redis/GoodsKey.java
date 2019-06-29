package com.shu.miaosha.redis;

/**
 * @author yang
 * @date 2019/6/28 14:17
 */
public class GoodsKey extends BasePrefix {
    public GoodsKey(Integer expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static GoodsKey getGoodsList = new GoodsKey(60, "gl");
    public static GoodsKey getGoodsDetail = new GoodsKey(60, "gd");
}
