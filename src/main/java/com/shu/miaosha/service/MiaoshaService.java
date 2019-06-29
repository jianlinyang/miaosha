package com.shu.miaosha.service;

import com.shu.miaosha.domain.MiaoshaUser;
import com.shu.miaosha.domain.OrderInfo;
import com.shu.miaosha.vo.GoodsVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yang
 * @date 2019/6/29 15:20
 */
@Service
public class MiaoshaService {
    private final GoodsService goodsService;
    private final OrderService orderService;

    public MiaoshaService(GoodsService goodsService, OrderService orderService) {
        this.goodsService = goodsService;
        this.orderService = orderService;
    }

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        goodsService.reduceStock(goods);
        return orderService.createOrder(user, goods);
    }
}
