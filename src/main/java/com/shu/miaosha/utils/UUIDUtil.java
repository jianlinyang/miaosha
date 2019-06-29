package com.shu.miaosha.utils;

import java.util.UUID;

/**
 * @author yang
 * @date 2019/6/29 9:23
 */
public class UUIDUtil {
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
