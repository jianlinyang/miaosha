package com.shu.miaosha.utils;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yang
 * @date 2019/6/28 21:05
 */
public class ValidatorUtil {
    public static final Pattern MOBILE_PATTERN = Pattern.compile("1\\d{10}");
    public static Boolean isMobile(String str){
        if(StringUtils.isEmpty(str)){
            return false;
        }
        Matcher matcher = MOBILE_PATTERN.matcher(str);
        return matcher.matches();
    }
}
