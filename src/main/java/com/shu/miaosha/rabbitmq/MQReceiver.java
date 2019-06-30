package com.shu.miaosha.rabbitmq;

import com.shu.miaosha.domain.MiaoshaOrder;
import com.shu.miaosha.domain.MiaoshaUser;
import com.shu.miaosha.redis.RedisService;
import com.shu.miaosha.service.GoodsService;
import com.shu.miaosha.service.MiaoshaService;
import com.shu.miaosha.service.MiaoshaUserService;
import com.shu.miaosha.service.OrderService;
import com.shu.miaosha.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @author yang
 * @date 2019/6/29 21:16
 */
@Service
@Slf4j
public class MQReceiver {
    private final GoodsService goodsService;
    private final RedisService redisService;
    private final MiaoshaUserService miaoshaUserService;
    //作为秒杀功能事务的Service
    private final MiaoshaService miaoshaService;
    private final OrderService orderService;

    public MQReceiver(GoodsService goodsService, RedisService redisService, MiaoshaUserService miaoshaUserService, MiaoshaService miaoshaService, OrderService orderService) {
        this.goodsService = goodsService;
        this.redisService = redisService;
        this.miaoshaUserService = miaoshaUserService;
        this.miaoshaService = miaoshaService;
        this.orderService = orderService;
    }


    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)//指明监听的是哪一个queue
    public void receiveMiaosha(String message) {
        log.info("receiveMiaosha message:" + message);
        //通过string类型的message还原成bean
        //拿到了秒杀信息之后。开始业务逻辑秒杀，
        MiaoshaMessage mm = RedisService.stringToBean(message, MiaoshaMessage.class);
        if (mm == null) {
            return;
        }
        MiaoshaUser user = mm.getUser();
        long goodsId = mm.getGoodsId();
        GoodsVo goodsvo = goodsService.getGoodsVoByGoodsId(goodsId);
        int stockcount = goodsvo.getStockCount();
        //1.判断库存不足
        if (stockcount <= 0) {//失败			库存至临界值1的时候，此时刚好来了加入10个线程，那么库存就会-10
            //model.addAttribute("errorMessage", CodeMsg.MIAOSHA_OVER_ERROR);
            return;
        }
        //2.判断这个秒杀订单形成没有，判断是否已经秒杀到了，避免一个账户秒杀多个商品
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {// 重复下单
            // model.addAttribute("errorMessage", CodeMsg.REPEATE_MIAOSHA);
            return;
        }
        //原子操作：1.库存减1，2.下订单，3.写入秒杀订单--->是一个事务
        //miaoshaService.miaosha(user,goodsvo);
        miaoshaService.miaosha(user, goodsvo);
    }
}
