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

    //登录模块

    //商品模块

    //订单模块

    //秒杀模块
}
