package com.shu.miaosha.controller;

import com.shu.miaosha.domain.MiaoshaUser;
import com.shu.miaosha.redis.RedisService;
import com.shu.miaosha.service.GoodsService;
import com.shu.miaosha.service.MiaoshaUserService;
import com.shu.miaosha.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author yang
 * @date 2019/6/29 9:35
 */
@Controller
@Slf4j
@RequestMapping("/goods")
public class GoodsController {

    private final GoodsService goodsService;
    private final RedisService redisService;
    private final MiaoshaUserService userService;

    public GoodsController(RedisService redisService, MiaoshaUserService userService, GoodsService goodsService) {
        this.redisService = redisService;
        this.userService = userService;
        this.goodsService = goodsService;
    }

    @RequestMapping("/to_list")
    public String toLogin(Model model, MiaoshaUser user) {
        model.addAttribute("user", user);
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsVos);
        return "goods_list_old";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model, MiaoshaUser user,
                         @PathVariable("goodsId") long goodId) {
        model.addAttribute("user", user);
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodId);
        model.addAttribute("goods", goods);
        long start = goods.getStartDate().getTime();
        long end = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (now < start) {
            miaoshaStatus = 0;
            remainSeconds = (int) (start - now) / 1000;
        } else if (now > end) {
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        return "goods_detail";
    }
}
