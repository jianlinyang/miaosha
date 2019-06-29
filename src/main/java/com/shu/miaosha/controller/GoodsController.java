package com.shu.miaosha.controller;

import com.shu.miaosha.domain.MiaoshaUser;
import com.shu.miaosha.redis.GoodsKey;
import com.shu.miaosha.redis.RedisService;
import com.shu.miaosha.result.Result;
import com.shu.miaosha.service.GoodsService;
import com.shu.miaosha.service.MiaoshaUserService;
import com.shu.miaosha.vo.GoodsDetailVo;
import com.shu.miaosha.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
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
    private final ThymeleafViewResolver thymeleafViewResolver;
    private final ApplicationContext applicationContext;

    public GoodsController(RedisService redisService, MiaoshaUserService userService, GoodsService goodsService, ThymeleafViewResolver thymeleafViewResolver, ApplicationContext applicationContext) {
        this.redisService = redisService;
        this.userService = userService;
        this.goodsService = goodsService;
        this.thymeleafViewResolver = thymeleafViewResolver;
        this.applicationContext = applicationContext;
    }

    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String toLogin(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user) {
        model.addAttribute("user", user);
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsVos);
        //取缓存
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        //手动渲染
        SpringWebContext context = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", context);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }

    @RequestMapping(value = "/to_detail2/{goodsId}", produces = "text/html")
    @ResponseBody
    public String detail2(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user,
                          @PathVariable("goodsId") long goodId) {
        //取缓存
        String html = redisService.get(GoodsKey.getGoodsDetail, "" + goodId, String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
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

        //手动渲染
        SpringWebContext context = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", context);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsDetail, "" + goodId, html);
        }
        return html;
    }

    @RequestMapping(value = "/to_detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user,
                                        @PathVariable("goodsId") long goodId) {

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodId);
        long start = goods.getStartDate().getTime();
        long end = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
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
        goodsDetailVo.setGoodsVo(goods);
        goodsDetailVo.setMiaoshaUser(user);
        goodsDetailVo.setRemainSeconds(remainSeconds);
        goodsDetailVo.setMiaoshaStatus(miaoshaStatus);

        return Result.success(goodsDetailVo);
    }
}
