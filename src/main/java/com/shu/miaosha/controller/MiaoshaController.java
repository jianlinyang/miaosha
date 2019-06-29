package com.shu.miaosha.controller;

import com.shu.miaosha.domain.MiaoshaOrder;
import com.shu.miaosha.domain.MiaoshaUser;
import com.shu.miaosha.domain.OrderInfo;
import com.shu.miaosha.redis.RedisService;
import com.shu.miaosha.result.CodeMsg;
import com.shu.miaosha.service.GoodsService;
import com.shu.miaosha.service.MiaoshaService;
import com.shu.miaosha.service.MiaoshaUserService;
import com.shu.miaosha.service.OrderService;
import com.shu.miaosha.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author yang
 * @date 2019/6/29 15:08
 */
@Controller
@Slf4j
@RequestMapping("/miaosha")
public class MiaoshaController {
    private final GoodsService goodsService;
    private final RedisService redisService;
    private final MiaoshaUserService userService;
    private final OrderService orderService;
    private final MiaoshaService miaoshaService;

    public MiaoshaController(RedisService redisService, MiaoshaUserService userService, GoodsService goodsService, OrderService orderService, MiaoshaService miaoshaService) {
        this.redisService = redisService;
        this.userService = userService;
        this.goodsService = goodsService;
        this.orderService = orderService;
        this.miaoshaService = miaoshaService;
    }

    @RequestMapping("/do_miaosha")
    public String list(Model model, MiaoshaUser user,
                       @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return "login";
        }
        model.addAttribute("user", user);
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        Integer stockCount = goods.getStockCount();
        //判断是否有库存
        if (stockCount <= 0) {
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            return "miaosha_fail";
        }
        //判断是否秒杀到
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            model.addAttribute("errmsg", CodeMsg.REPEATE_MIAO_SHA);
            return "miaosha_fail";
        }
        //减库存 下订单
        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);
        return "order_detail";
    }
}
