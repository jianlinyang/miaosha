package com.shu.miaosha.vo;

import com.shu.miaosha.domain.MiaoshaUser;
import lombok.Data;

/**
 * @author yang
 * @date 2019/6/29 18:51
 */
@Data
public class GoodsDetailVo {
    private int miaoshaStatus ;
    private int remainSeconds ;
    private GoodsVo goods;
    private MiaoshaUser user;
}
