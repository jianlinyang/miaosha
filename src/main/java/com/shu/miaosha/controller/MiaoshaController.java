package com.shu.miaosha.controller;

import com.shu.miaosha.access.AccessLimit;
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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
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
public class MiaoshaController implements InitializingBean {
    private final GoodsService goodsService;
    private final RedisService redisService;
    private final MiaoshaUserService userService;
    private final OrderService orderService;
    private final MiaoshaService miaoshaService;
    private final MQSender sender;

    private Map<Long, Boolean> localOverMap = new HashMap<>();

    public MiaoshaController(RedisService redisService, MiaoshaUserService userService, GoodsService goodsService, OrderService orderService, MiaoshaService miaoshaService, MQSender sender) {
        this.redisService = redisService;
        this.userService = userService;
        this.goodsService = goodsService;
        this.orderService = orderService;
        this.miaoshaService = miaoshaService;
        this.sender = sender;
    }

    /**
     * 初始化
     *
     * @throws Exception
     */

    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if (goodsList == null) {
            return;
        }
        for (GoodsVo goods : goodsList) {
            //如果不是null的时候，将库存加载到redis里面去
            redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goods.getId(), goods.getStockCount());
            localOverMap.put(goods.getId(), false);
        }
    }

    /**
     * 生成图片验证码
     */
    @GetMapping(value = "/vertifyCode")
    @ResponseBody
    public Result<String> getVertifyCode(Model model, MiaoshaUser user,
                                         @RequestParam("goodsId") Long goodsId, HttpServletResponse response) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        BufferedImage img = miaoshaService.createMiaoshaVertifyCode(user, goodsId);
        try {
            OutputStream out = response.getOutputStream();
            ImageIO.write(img, "JPEG", out);
            out.flush();
            out.close();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }
    }

    /**
     * 获取秒杀的path,并且验证验证码的值是否正确
     */
    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @RequestMapping(value = "/getPath")
    @ResponseBody
    public Result<String> getMiaoshaPath(MiaoshaUser user,
                                         @RequestParam("goodsId") Long goodsId,
                                         @RequestParam(value = "vertifyCode", defaultValue = "0") int vertifyCode) {
        //如果用户为空，则返回至登录页面
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //验证验证码
        boolean check = miaoshaService.checkVCode(user, goodsId, vertifyCode);
        if (!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        //生成一个随机串
        String path = miaoshaService.createMiaoshaPath(user, goodsId);
        return Result.success(path);
    }

    /**
     * 客户端做一个轮询，查看是否成功与失败，失败了则不用继续轮询。
     * 秒杀成功，返回订单的Id。
     * 库存不足直接返回-1。
     * 排队中则返回0。
     * 查看是否生成秒杀订单。
     */
    @GetMapping(value = "/result")
    @ResponseBody
    public Result<Long> doMiaoshaResult(MiaoshaUser user,
                                        @RequestParam(value = "goodsId", defaultValue = "0") long goodsId) {
        long result = miaoshaService.getMiaoshaResult(user.getId(), goodsId);
        log.info("轮询 result：" + result);
        return Result.success(result);
    }

    @PostMapping("/{path}/do_miaosha")
    @ResponseBody
    public Result<Integer> list(MiaoshaUser user,
                                @RequestParam("goodsId") long goodsId,
                                @PathVariable("path") String path) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //验证path
        boolean b = miaoshaService.checkPath(user, goodsId, path);
        if (!b) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
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
