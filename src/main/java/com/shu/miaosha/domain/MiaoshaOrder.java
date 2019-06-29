package com.shu.miaosha.domain;

import lombok.Data;

/**
 * @author yang
 */
@Data
public class MiaoshaOrder {
	private Long id;
	private Long userId;
	private Long orderId;
	private Long goodsId;
}
