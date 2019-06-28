package com.shu.miaosha.controller;

import com.shu.miaosha.redis.RedisService;
import com.shu.miaosha.result.CodeMsg;
import com.shu.miaosha.result.Result;
import com.shu.miaosha.service.MiaoshaUserService;
import com.shu.miaosha.utils.ValidatorUtil;
import com.shu.miaosha.vo.LoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public Result doLogin(@Validated LoginVO loginVO) {
        log.info(loginVO.toString());
        //参数校验
//        String password = loginVO.getPassword();
//        String mobile = loginVO.getMobile();
//        if (StringUtils.isEmpty(password)) {
//            return Result.error(CodeMsg.PASSWORD_EMPTY);
//        }
//        if (StringUtils.isEmpty(mobile)) {
//            return Result.error(CodeMsg.MOBILE_EMPTY);
//        }
//        if (!ValidatorUtil.isMobile(mobile)) {
//            return Result.error(CodeMsg.MOBILE_ERROR);
//        }
        //登录
        CodeMsg codeMsg = userService.login(loginVO);
        if (codeMsg.getCode() == 0) {
            return Result.success(true);
        } else {
            return Result.error(codeMsg);
        }

    }
}
