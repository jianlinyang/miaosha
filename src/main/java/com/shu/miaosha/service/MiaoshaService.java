package com.shu.miaosha.service;

import com.shu.miaosha.domain.MiaoshaOrder;
import com.shu.miaosha.domain.MiaoshaUser;
import com.shu.miaosha.domain.OrderInfo;
import com.shu.miaosha.redis.MiaoshaKey;
import com.shu.miaosha.redis.RedisService;
import com.shu.miaosha.utils.MD5Util;
import com.shu.miaosha.utils.UUIDUtil;
import com.shu.miaosha.vo.GoodsVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author yang
 * @date 2019/6/29 15:20
 */
@Service
public class MiaoshaService {
    private final GoodsService goodsService;
    private final OrderService orderService;
    private final RedisService redisService;

    public MiaoshaService(GoodsService goodsService, OrderService orderService, RedisService redisService) {
        this.goodsService = goodsService;
        this.orderService = orderService;
        this.redisService = redisService;
    }

    /**
     * 获取秒杀结果
     * 成功返回id
     * 失败返回0或-1
     * 0代表排队中
     * -1代表库存不足
     *
     * @param userId
     * @param goodsId
     * @return
     */
    public long getMiaoshaResult(Long userId, long goodsId) {
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
        //秒杀成功
        if (order != null) {
            return order.getOrderId();
        } else {
            //查看商品是否卖完了
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {//商品卖完了
                return -1;
            } else {        //商品没有卖完
                return 0;
            }
        }
    }

    private boolean getGoodsOver(Long goodsId) {
        return redisService.exist(MiaoshaKey.isGoodsOver, "" + goodsId);
    }
    /**
     * redisService.set(MiaoshaKey.getMiaoshaPath, ""+user.getId()+"_"+goodsId, str);
     * 去缓存里面检查path是否正确，验证path。
     */
    /**
     * 5-22
     * 先写入缓存
     */
    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver, "" + goodsId, true);
    }

    /**
     * 验证
     *
     * @param user    {@link MiaoshaUser}
     * @param goodsId 商品 id
     * @param path    秒杀路径
     * @return true/false
     */
    public boolean checkPath(MiaoshaUser user, long goodsId, String path) {
        if (user == null || path == null) {
            return false;
        }
        String pathRedis = redisService.get(MiaoshaKey.getMiaoshaPath, "" + user.getId() + "_" + goodsId, String.class);
        return path.equals(pathRedis);
    }

    /**
     * 生成一个秒杀path，写入缓存，并且，返回至前台
     */
    public String createMiaoshaPath(MiaoshaUser user, Long goodsId) {
        String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
        //将随机串保存在客户端，并且返回至客户端。
        //String path=""+user.getId()+"_"+goodsId;
        redisService.set(MiaoshaKey.getMiaoshaPath, "" + user.getId() + "_" + goodsId, str);
        return str;
    }

    public BufferedImage createMiaoshaVertifyCode(MiaoshaUser user, Long goodsId) {
        if (user == null || goodsId <= 0) {
            return null;
        }
        int width = 80;
        int height = 30;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width - 1, height - 1);
        Random rdm = new Random();
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        //生成验证码
        String vertifyCode = createVertifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        //将验证码写在图片上
        g.drawString(vertifyCode, 8, 24);
        g.dispose();
        //计算存值
        int rnd = calc(vertifyCode);
        //将计算结果保存到redis上面去
        redisService.set(MiaoshaKey.getMiaoshaVertifyCode, "" + user.getId() + "_" + goodsId, rnd);
        return img;
    }

    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer) engine.eval(exp);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static char[] ops = new char[]{'+', '-', '*'};

    /**
     * + - *
     */
    private String createVertifyCode(Random rdm) {
        //生成10以内的
        int n1 = rdm.nextInt(10);
        int n2 = rdm.nextInt(10);
        int n3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];//0  1  2
        char op2 = ops[rdm.nextInt(3)];//0  1  2
        String exp = "" + n1 + op1 + n2 + op2 + n3;
        return exp;
    }

    /**
     * 验证验证码，取缓存里面取得值，验证是否相等
     */
    public boolean checkVCode(MiaoshaUser user, Long goodsId, int vertifyCode) {
        if (user == null || goodsId <= 0) {
            return false;
        }
        Integer redisVCode = redisService.get(MiaoshaKey.getMiaoshaVertifyCode, user.getId() + "_" + goodsId, Integer.class);
        if (redisVCode == null || redisVCode - vertifyCode != 0) {
            return false;
        }
        //删除缓存里面的数据
        redisService.delete(MiaoshaKey.getMiaoshaVertifyCode, user.getId() + "_" + goodsId);
        return true;
    }

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        boolean success = goodsService.reduceStock(goods);
        if (success) {
            return orderService.createOrder(user, goods);
        } else {
            setGoodsOver(goods.getId());
            return null;
        }
    }
}
