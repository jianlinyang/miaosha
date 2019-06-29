package com.shu.miaosha.vo;

import com.shu.miaosha.domain.Goods;
import lombok.Data;

import java.util.Date;

/**
 * 将Goods表和MiaoshaGoods表合并
 *
 * @author yang
 */
@Data
public class GoodsVo extends Goods {
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
    private Double miaoshaPrice;
}
