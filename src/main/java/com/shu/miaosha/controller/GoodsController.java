package com.shu.miaosha.controller;

import com.shu.miaosha.domain.MiaoshaUser;
import com.shu.miaosha.redis.RedisService;
import com.shu.miaosha.service.MiaoshaUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

/**
 * @author yang
 * @date 2019/6/29 9:35
 */
@Controller
@Slf4j
@RequestMapping("/goods")
public class GoodsController {
    private final RedisService redisService;
    private final MiaoshaUserService userService;

    public GoodsController(RedisService redisService, MiaoshaUserService userService) {
        this.redisService = redisService;
        this.userService = userService;
    }

    @RequestMapping("/to_list")
    public String toLogin(Model model, MiaoshaUser user) {
        model.addAttribute("user", user);
        return "goods_list_old";
    }

    @RequestMapping("/to_detail")
    public String detail(HttpServletResponse response, Model model,
                         @CookieValue(value = MiaoshaUserService.COOKIE_NAME_TOKEN, required = false) String cookieToken,
                         @RequestParam(value = MiaoshaUserService.COOKIE_NAME_TOKEN, required = false) String paramToken) {
        return null;
    }
}
