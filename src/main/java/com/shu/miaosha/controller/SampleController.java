package com.shu.miaosha.controller;

import com.shu.miaosha.domain.User;
import com.shu.miaosha.redis.RedisService;
import com.shu.miaosha.redis.UserKey;
import com.shu.miaosha.result.Result;
import com.shu.miaosha.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yang
 * @date 2019/6/27 23:50
 */
@Controller
@RequestMapping("/demo")
public class SampleController {
    private final UserService userService;
    private final RedisService redisService;

    public SampleController(UserService userService, RedisService redisService) {
        this.userService = userService;
        this.redisService = redisService;
    }

    @ResponseBody
    @RequestMapping("/db/get")
    public Result<User> dbgGet() {
        User user = userService.getById(1);
        return Result.success(user);
    }

    @ResponseBody
    @RequestMapping("/redis/get")
    public Result<User> redisGet() {
        User user = new User(2, "shui");
        redisService.set(UserKey.getById, String.valueOf(user.getId()), user);
        User v1 = redisService.get(UserKey.getById, String.valueOf(user.getId()), User.class);
        return Result.success(v1);
    }
}
