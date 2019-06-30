package com.shu.miaosha.controller;

import com.shu.miaosha.domain.MiaoshaUser;
import com.shu.miaosha.redis.RedisService;
import com.shu.miaosha.result.CodeMsg;
import com.shu.miaosha.result.Result;
import com.shu.miaosha.service.MiaoshaUserService;
import com.shu.miaosha.utils.MD5Util;
import com.shu.miaosha.vo.LoginVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author yang
 * @date 2019/6/29 17:04
 */
@Controller
public class UserController {
    private final MiaoshaUserService userService;
    private final RedisService redisService;

    public UserController(MiaoshaUserService userService, RedisService redisService) {
        this.userService = userService;
        this.redisService = redisService;
    }

    @RequestMapping("/register")
    public String register() {
        return "to_register";
    }

    @PostMapping("/do_register")
    @ResponseBody
    public Result<Boolean> doRegister(@RequestParam(name = "mobile") Long mobile,
                                      @RequestParam(name = "username") String username,
                                      @RequestParam(name = "password") String password,
                                      HttpServletResponse response) {
        MiaoshaUser byId = userService.getById(mobile);
        if (byId != null) {
            return Result.error(CodeMsg.USER_EXIST);
        }
        MiaoshaUser miaoshaUser = new MiaoshaUser();
        miaoshaUser.setNickname(username);
        miaoshaUser.setSalt("1a2b3c4d");
        miaoshaUser.setId(mobile);
        miaoshaUser.setPassword(MD5Util.inputPassToDbPass(password, miaoshaUser.getSalt()));
        miaoshaUser.setRegisterDate(new Date());
        userService.insert(miaoshaUser);
        LoginVO loginVO = new LoginVO();
        loginVO.setMobile(String.valueOf(miaoshaUser.getId()));
        loginVO.setPassword(MD5Util.inputPassFormPass(password));
        userService.login(response, loginVO);
        return Result.success(true);
    }
}
