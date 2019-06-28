package com.shu.miaosha.redis;

/**
 * @author yang
 * @date 2019/6/28 14:11
 */
public interface KeyPrefix {
    /**
     * 过期时间
     *
     * @return {@link Integer}
     */
    Integer expireSeconds();

    /**
     * 获取前缀
     *
     * @return {@link String}
     */
    String getPrefix();
}
