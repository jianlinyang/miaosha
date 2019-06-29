package com.shu.miaosha.vo;

import com.shu.miaosha.domain.OrderInfo;
import lombok.Data;

/**
 * @author yang
 * @date 2019/6/29 20:33
 */
@Data
public class OrderDetailVo {
    private GoodsVo goodsVo;
    private OrderInfo order;
}
