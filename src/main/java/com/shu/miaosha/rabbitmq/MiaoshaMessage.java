package com.shu.miaosha.rabbitmq;

import com.shu.miaosha.domain.MiaoshaUser;
import lombok.Data;

/**
 * @author yang
 * @date 2019/6/29 23:19
 */
@Data
public class MiaoshaMessage {
    private MiaoshaUser user;
    private long goodsId;
}
