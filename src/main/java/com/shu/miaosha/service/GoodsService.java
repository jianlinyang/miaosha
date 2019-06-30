package com.shu.miaosha.service;

import com.shu.miaosha.dao.GoodsDao;
import com.shu.miaosha.domain.MiaoshaGoods;
import com.shu.miaosha.vo.GoodsVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yang
 * @date 2019/6/29 12:33
 */
@Service
public class GoodsService {
    private final GoodsDao goodsDao;

    public GoodsService(GoodsDao goodsDao) {
        this.goodsDao = goodsDao;
    }

    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodId) {
        return goodsDao.getGoodsVoByGoodsId(goodId);
    }

    public boolean reduceStock(GoodsVo goods) {
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goods.getId());
        int res = goodsDao.reduceStock(g);
        return res > 0;
    }
}
