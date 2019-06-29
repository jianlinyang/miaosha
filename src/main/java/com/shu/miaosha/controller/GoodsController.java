package com.shu.miaosha.controller;

import com.shu.miaosha.domain.MiaoshaUser;
import com.shu.miaosha.redis.RedisService;
import com.shu.miaosha.service.GoodsService;
import com.shu.miaosha.service.MiaoshaUserService;
import com.shu.miaosha.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
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

    @RequestMapping("/to_detail")
    public String detail(HttpServletResponse response, Model model,
                         @CookieValue(value = MiaoshaUserService.COOKIE_NAME_TOKEN, required = false) String cookieToken,
                         @RequestParam(value = MiaoshaUserService.COOKIE_NAME_TOKEN, required = false) String paramToken) {
        return null;
    }
}
