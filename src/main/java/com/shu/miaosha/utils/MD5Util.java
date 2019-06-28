package com.shu.miaosha.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author yang
 * @date 2019/6/28 16:37
 */
public class MD5Util {
    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";

    public static String inputPassToDbPass(String input, String saltDb) {
        return formPassDbPass(inputPassFormPass(input), saltDb);
    }

    public static String inputPassFormPass(String inputPass) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String formPassDbPass(String formPass, String salt) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }
}
