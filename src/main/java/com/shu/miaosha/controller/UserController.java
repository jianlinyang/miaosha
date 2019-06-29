package com.shu.miaosha.controller;

import com.shu.miaosha.domain.MiaoshaUser;
import com.shu.miaosha.redis.RedisService;
import com.shu.miaosha.result.Result;
import com.shu.miaosha.service.MiaoshaUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yang
 * @date 2019/6/29 17:04
 */
@Controller
@RequestMapping("/user")
public class UserController {
    private final MiaoshaUserService userService;
    private final RedisService redisService;

    public UserController(MiaoshaUserService userService, RedisService redisService) {
        this.userService = userService;
        this.redisService = redisService;
    }

    @RequestMapping("/info")
    @ResponseBody
    public Result<MiaoshaUser> info(Model model, MiaoshaUser user) {
        return Result.success(user);
    }
}
