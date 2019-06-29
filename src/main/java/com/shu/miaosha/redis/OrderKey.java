package com.shu.miaosha.redis;

/**
 * @author yang
 * @date 2019/6/28 14:17
 */
public class OrderKey extends BasePrefix {
    public OrderKey(String prefix) {
        super(prefix);
    }

    public static OrderKey getMiaoshaOrderByUidGid = new OrderKey("moug");
}
