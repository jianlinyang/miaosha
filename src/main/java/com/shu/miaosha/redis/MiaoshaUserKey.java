package com.shu.miaosha.redis;

/**
 * @author yang
 * @date 2019/6/28 14:16
 */
public class MiaoshaUserKey extends BasePrefix {
    public static final Integer TOKEN_EXPIRE = 3600 * 24 * 2;

    private MiaoshaUserKey(Integer expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static MiaoshaUserKey token = new MiaoshaUserKey(TOKEN_EXPIRE, "token");
    public static MiaoshaUserKey getById = new MiaoshaUserKey(0, "id");

}
