package com.shu.miaosha.redis;

import lombok.Data;

/**
 * @author yang
 * @date 2019/6/28 14:13
 */
@Data
public abstract class BasePrefix implements KeyPrefix {
    private Integer expireSeconds;
    private String prefix;

    public BasePrefix(String prefix) {
        this(0, prefix);
    }

    public BasePrefix(Integer expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    /**
     * 默认0代表永不过期
     *
     * @return
     */
    @Override
    public Integer expireSeconds() {
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }
}
