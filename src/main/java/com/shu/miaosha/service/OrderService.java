package com.shu.miaosha.service;

import com.shu.miaosha.dao.OrderDao;
import com.shu.miaosha.domain.MiaoshaOrder;
import com.shu.miaosha.domain.MiaoshaUser;
import com.shu.miaosha.domain.OrderInfo;
import com.shu.miaosha.redis.OrderKey;
import com.shu.miaosha.redis.RedisService;
import com.shu.miaosha.vo.GoodsVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author yang
 * @date 2019/6/29 15:14
 */
@Service
public class OrderService {
    private final OrderDao orderDao;
    private final RedisService redisService;

    public OrderService(OrderDao orderDao, RedisService redisService) {
        this.orderDao = orderDao;
        this.redisService = redisService;
    }

    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(Long id, long goodsId) {
        return redisService.get(OrderKey.getMiaoshaOrderByUidGid, "" + id + "_" + goodsId, MiaoshaOrder.class);
    }

    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        long orderId = orderDao.insert(orderInfo);
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrder.setOrderId(orderId);
        miaoshaOrder.setUserId(user.getId());
        orderDao.insertMiaoshaOrder(miaoshaOrder);
        redisService.set(OrderKey.getMiaoshaOrderByUidGid, "" + user.getId() + "_" + goods.getId(), MiaoshaOrder.class);
        return orderInfo;
    }

    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }
}
