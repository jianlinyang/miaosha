package com.shu.miaosha.controller;

import com.shu.miaosha.domain.MiaoshaUser;
import com.shu.miaosha.domain.OrderInfo;
import com.shu.miaosha.result.CodeMsg;
import com.shu.miaosha.result.Result;
import com.shu.miaosha.service.GoodsService;
import com.shu.miaosha.service.OrderService;
import com.shu.miaosha.vo.GoodsVo;
import com.shu.miaosha.vo.OrderDetailVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yang
 * @date 2019/6/29 20:31
 */
@RequestMapping("/order")
@Controller
public class OrderController {
    private final OrderService orderService;
    private final GoodsService goodsService;

    public OrderController(OrderService orderService, GoodsService goodsService) {
        this.orderService = orderService;
        this.goodsService = goodsService;
    }

    @GetMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(MiaoshaUser user, @RequestParam("orderId") long orderId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo order = orderService.getOrderById(orderId);
        if (order == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        Long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setOrder(order);
        orderDetailVo.setGoodsVo(goods);
        return Result.success(orderDetailVo);
    }
}
