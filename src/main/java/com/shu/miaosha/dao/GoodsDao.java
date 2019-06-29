package com.shu.miaosha.dao;

import com.shu.miaosha.domain.MiaoshaGoods;
import com.shu.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author yang
 * @date 2019/6/29 12:34
 */
@Mapper
public interface GoodsDao {
    @Select("select g.*,mg.stock_count,mg.start_date,mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id=g.id")
    List<GoodsVo> listGoodsVo();
    @Select("select g.*,mg.stock_count,mg.start_date,mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id=g.id where g.id=#{goodId}")
    GoodsVo getGoodsVoByGoodsId(@Param("goodId") long goodId);
    @Update("update miaosha_goods set stock_count=stock_count-1 where goods_id=#{id}")
    void reduceStock(MiaoshaGoods g);
}
