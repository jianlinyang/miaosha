package com.shu.miaosha.domain;

import lombok.Data;

/**
 * @author yang
 */
@Data
public class Goods {
	private Long id;
	private String goodsName;
	private String goodsTitle;
	private String goodsImg;
	private String goodsDetail;
	private Double goodsPrice;
	//库存
	private Integer goodsStock;
}
