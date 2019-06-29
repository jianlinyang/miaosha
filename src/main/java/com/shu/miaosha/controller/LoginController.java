package com.shu.miaosha.controller;

import com.shu.miaosha.redis.RedisService;
import com.shu.miaosha.result.Result;
import com.shu.miaosha.service.MiaoshaUserService;
import com.shu.miaosha.vo.LoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * <h1>登录controller</h1>
 *
 * @author yang
 * @date 2019/6/28 16:58
 */
@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    private final RedisService redisService;
    private final MiaoshaUserService userService;

    public LoginController(RedisService redisService, MiaoshaUserService userService) {
        this.redisService = redisService;
        this.userService = userService;
    }

    @GetMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @ResponseBody
    @RequestMapping("/do_login")
    public Result doLogin(HttpServletResponse response, @Validated LoginVO loginVO) {
        log.info(loginVO.toString());

        //登录
        userService.login(response, loginVO);
        return Result.success(true);

    }
}
