package com.shu.miaosha.redis;

/**
 * @author yang
 * @date 2019/6/28 14:16
 */
public class UserKey extends BasePrefix {
    private UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey getById=new UserKey("id");
    public static UserKey getByName=new UserKey("name");

}
