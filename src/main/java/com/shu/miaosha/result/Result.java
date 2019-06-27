package com.shu.miaosha.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>统一返回结果集</h1>
 *
 * @author yang
 * @date 2019/6/27 19:03
 */
@Data
public class Result<T> {
    /**
     * 返回码
     */
    private Integer code;
    /**
     * 消息
     */
    private String msg;
    /**
     * 数据对象
     */
    private T data;


    private Result(T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }

    private Result(CodeMsg msg) {
        if (msg == null) {
            return;
        }
        this.code = msg.getCode();
        this.msg = msg.getMsg();
    }

    /**
     * 成功时调用
     *
     * @param data data
     * @param <T>  T
     * @return T
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(data);
    }

    /**
     * 失败时调用
     *
     * @param msg {@link CodeMsg}
     * @param <T> T
     * @return T
     */
    public static <T> Result<T> error(CodeMsg msg) {
        return new Result<T>(msg);
    }
}
