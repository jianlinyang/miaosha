package com.shu.miaosha.access;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author yang
 * @date 2019/6/29 23:48
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface AccessLimit {
	int seconds();
	int maxCount();
	boolean needLogin() default true;
}
