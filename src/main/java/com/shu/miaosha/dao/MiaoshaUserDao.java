package com.shu.miaosha.dao;

import com.shu.miaosha.domain.MiaoshaUser;
import org.apache.ibatis.annotations.*;

/**
 * @author yang
 * @date 2019/6/28 23:29
 */
@Mapper
public interface MiaoshaUserDao {
    @Select("select * from miaosha_user where id = #{id}")
    MiaoshaUser getById(@Param("id") Long id);

    @Update("update miaosha_user set password = #{password} where id = #{id}")
    void update(MiaoshaUser toBeUpdate);

    @Insert("insert into miaosha_user (login_count,nickname,register_date,salt,password,id,head) values " +
            "(#{loginCount},#{nickname},#{registerDate},#{salt},#{password},#{id},#{head})")
    boolean insert(MiaoshaUser miaoshaUser);
}
