package com.shu.miaosha.access;

import com.alibaba.fastjson.JSON;
import com.shu.miaosha.domain.MiaoshaUser;
import com.shu.miaosha.redis.AccessKey;
import com.shu.miaosha.redis.RedisService;
import com.shu.miaosha.result.CodeMsg;
import com.shu.miaosha.result.Result;
import com.shu.miaosha.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author yang
 * @date 2019/6/29 23:51
 */
@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {
    private final MiaoshaUserService miaoshaUserService;
    private final RedisService redisService;

    public AccessInterceptor(MiaoshaUserService miaoshaUserService, RedisService redisService) {
        this.miaoshaUserService = miaoshaUserService;
        this.redisService = redisService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            //先去取得用户做判断
            MiaoshaUser user = getUser(request, response);

            //将user保存下来
            UserContext.setUser(user);
            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            //无该注解的时候，那么就不进行拦截操作
            if (accessLimit == null) {
                return true;
            }
            //获取参数
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key = request.getRequestURI();
            if (needLogin) {
                if (user == null) {
                    //需要给客户端一个提示
                    render(response, CodeMsg.SESSION_ERROR);
                    return false;
                }
                //需要的登录
                key += "_" + user.getId();
            }
            //限制访问次数
            String uri = request.getRequestURI();
            //限定key5s之内只能访问5次，动态设置有效期
            AccessKey akey = AccessKey.expire(seconds);
            Integer count = redisService.get(akey, key, Integer.class);
            if (count == null) {
                redisService.set(akey, key, 1);
            } else if (count < maxCount) {
                redisService.incr(akey, key);
            } else {//超过5次
                render(response, CodeMsg.ACCESS_LIMIT);
                //结果给前端
                return false;
            }
        }
        return super.preHandle(request, response, handler);
    }

    private void render(HttpServletResponse response, CodeMsg codeMsg) throws IOException {
        //指定输出的编码格式，避免乱码
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String jsonString = JSON.toJSONString(Result.error(codeMsg));
        out.write(jsonString.getBytes("UTF-8"));
        out.flush();
        out.close();
    }

    private MiaoshaUser getUser(HttpServletRequest request, HttpServletResponse response) {
        String paramToken = request.getParameter(MiaoshaUserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request, MiaoshaUserService.COOKIE_NAME_TOKEN);
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        return miaoshaUserService.getByToken(response, token);
    }

    private String getCookieValue(HttpServletRequest request, String cookieNameToken) {
        //遍历request里面所有的cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieNameToken)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
