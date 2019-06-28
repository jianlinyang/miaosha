package com.shu.miaosha.dao;

import com.shu.miaosha.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author yang
 * @date 2019/6/28 23:29
 */
@Mapper
public interface MiaoshaUserDao {
    @Select("select * from miaosha_user where id = #{id}")
    MiaoshaUser getById(@Param("id") Long id);

}
