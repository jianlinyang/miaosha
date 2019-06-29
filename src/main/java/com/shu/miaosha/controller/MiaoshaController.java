package com.shu.miaosha.controller;

import com.shu.miaosha.domain.MiaoshaOrder;
import com.shu.miaosha.domain.MiaoshaUser;
import com.shu.miaosha.rabbitmq.MQSender;
import com.shu.miaosha.rabbitmq.MiaoshaMessage;
import com.shu.miaosha.redis.GoodsKey;
import com.shu.miaosha.redis.RedisService;
import com.shu.miaosha.result.CodeMsg;
import com.shu.miaosha.result.Result;
import com.shu.miaosha.service.GoodsService;
import com.shu.miaosha.service.MiaoshaService;
import com.shu.miaosha.service.MiaoshaUserService;
import com.shu.miaosha.service.OrderService;
import com.shu.miaosha.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final MQSender sender;

    private Map<Long, Boolean> localOverMap = new HashMap<>();

    /**
     * 初始化
     *
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodslist = goodsService.listGoodsVo();
        if (goodslist == null) {
            return;
        }
        for (GoodsVo goods : goodslist) {
            //如果不是null的时候，将库存加载到redis里面去
            redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goods.getId(), goods.getStockCount());
            localOverMap.put(goods.getId(), false);
        }
    }

    public MiaoshaController(RedisService redisService, MiaoshaUserService userService, GoodsService goodsService, OrderService orderService, MiaoshaService miaoshaService, MQSender sender) {
        this.redisService = redisService;
        this.userService = userService;
        this.goodsService = goodsService;
        this.orderService = orderService;
        this.miaoshaService = miaoshaService;
        this.sender = sender;
    }

    @PostMapping("/do_miaosha")
    @ResponseBody
    public Result<Integer> list(Model model, MiaoshaUser user,
                                @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        Boolean flag = localOverMap.get(goodsId);
        if (flag) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        Long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
        if (stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否秒杀到
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEATE_MIAO_SHA);
        }
        //入队
        MiaoshaMessage miaoshaMessage = new MiaoshaMessage();
        miaoshaMessage.setUser(user);
        miaoshaMessage.setGoodsId(goodsId);
        sender.sendMiaoshaMessage(miaoshaMessage);
        //减库存 下订单
        return Result.success(0);
    }
}
