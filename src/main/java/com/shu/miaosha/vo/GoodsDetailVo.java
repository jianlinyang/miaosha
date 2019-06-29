package com.shu.miaosha.vo;

import com.shu.miaosha.domain.MiaoshaUser;
import lombok.Data;

/**
 * @author yang
 * @date 2019/6/29 18:51
 */
@Data
public class GoodsDetailVo {
    private int miaoshaStatus = 0;
    private int remainSeconds = 0;
    private GoodsVo goodsVo;
    private MiaoshaUser miaoshaUser;
}
