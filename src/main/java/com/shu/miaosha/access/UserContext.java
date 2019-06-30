package com.shu.miaosha.access;

import com.shu.miaosha.domain.MiaoshaUser;

/**
 * @author yang
 * @date 2019/6/29 23:59
 */
public class UserContext {
	private static ThreadLocal<MiaoshaUser> userHolder=new ThreadLocal<>();
	
	public static void setUser(MiaoshaUser user) {
		userHolder.set(user);
	}
	
	public static MiaoshaUser getUser() {
		return userHolder.get();
	}
}
