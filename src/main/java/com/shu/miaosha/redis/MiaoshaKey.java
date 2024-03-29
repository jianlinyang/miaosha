package com.shu.miaosha.redis;

/**
 * @author yang
 * @date 2019/6/30 9:11
 */
public class MiaoshaKey extends BasePrefix{
	//考虑页面缓存有效期比较短
	private MiaoshaKey(int expireSeconds, String prefix) {
		super(expireSeconds,prefix);
	}
	public static MiaoshaKey isGoodsOver=new MiaoshaKey(0,"go");
	//有效期60s
	public static MiaoshaKey getMiaoshaPath=new MiaoshaKey(60,"mp");
	//验证码   300s有效期
	public static MiaoshaKey getMiaoshaVertifyCode=new MiaoshaKey(300,"vc");
}
