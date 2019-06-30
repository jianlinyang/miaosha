package com.shu.miaosha.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>CodeMessage</h1>
 *
 * @author yang
 * @date 2019/6/27 19:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeMsg {
    private Integer code;
    private String msg;
    //通用异常
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常: %s");
    public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500102, "请求非法");

    //登录模块
    public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "SESSION不存在或者已失效");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "手机号不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500213, "手机号错误");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "手机号不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "密码错误");
    public static CodeMsg ACCESS_LIMIT = new CodeMsg(500216, "访问受限");


    //商品模块

    //订单模块
    public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500400, "订单不存在");

    //秒杀模块
    public static CodeMsg MIAO_SHA_OVER = new CodeMsg(500500, "商品库存不足");
    public static CodeMsg REPEATE_MIAO_SHA = new CodeMsg(500501, "不能重复秒杀");
    public static CodeMsg MIAOSHA_FAIL = new CodeMsg(500502, "秒杀失败");

    public CodeMsg fillArgs(Object... args) {
        String format = String.format(this.msg, args);
        return new CodeMsg(this.code, format);
    }
}
